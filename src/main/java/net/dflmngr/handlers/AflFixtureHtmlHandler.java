package net.dflmngr.handlers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import net.dflmngr.exceptions.AflFixtureException;
import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.AflTeam;
import net.dflmngr.model.service.AflTeamService;
import net.dflmngr.model.service.GlobalsService;

public class AflFixtureHtmlHandler extends BaseHandler {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE MMMM d h:mma yyyy");

    GlobalsService globalsService;
    AflTeamService aflTeamService;

    String currentYear;
    String defaultTimezone;

    static final String HTML_CLASS_STRING = "class";

    public AflFixtureHtmlHandler() {
        super("AflFixtureLoader");

        globalsService = serviceFactory.createGlobalsService();
        aflTeamService = serviceFactory.createAflTeamService();

        currentYear = globalsService.getCurrentYear();
        defaultTimezone = globalsService.getGroundTimeZone("default");
        ensureLoggingConfigured();
    }

    public void configureLogging(String logfile) {
        configureLogging(defaultMdcKey, defaultLoggerName, logfile);
    }

    public List<AflFixture> execute(Integer aflRound, String aflFixtureUrl) {

        loggerUtils.log("info", "Loading Afl Fixture HTML: aflRound={} url={}", aflRound, aflFixtureUrl);

        List<AflFixture> games = download(aflRound, aflFixtureUrl);

        loggerUtils.log("info", "Finished Loading Afl Fixture HTML");

        return games;
    }

    private List<AflFixture> download(Integer aflRound, String aflFixtureUrl) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--remote-allow-origins=*");

        int webdriverTimeout = globalsService.getWebdriverTimeout();
		int webdriverWait = globalsService.getWebdriverWait();
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(webdriverWait));
        chromeOptions.setPageLoadTimeout(Duration.ofSeconds(webdriverTimeout));

		WebDriver driver = new ChromeDriver(chromeOptions);

		driver.get(aflFixtureUrl);

        List<AflFixture> games = new ArrayList<>();

		int retry = 1;
		while(retry <= 5) {
			loggerUtils.log("info", "Try: {}", retry);
			if(driver.findElements(By.id("main-content")).isEmpty()) {
				loggerUtils.log("info", "Still waiting, will try again in 5");
				try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
			} else {
                try {
                    WebElement fixtureContent = driver.findElements(By.id("main-content")).get(0);
		            games = getFixtureData(aflRound, fixtureContent, aflFixtureUrl);
                } catch (Exception ex) {
                    loggerUtils.logException("Error loading AFL fxiture", ex);
                }
				if(!games.isEmpty()) {
                    break;
                }
			}
			retry++;
		}

        driver.quit();

        return games;
    }

    List<AflFixture> getFixtureData(int aflRound, WebElement fixtureContent, String aflFixtureUrl) {
        List<AflFixture> games = new ArrayList<>();
        
        List<WebElement> fixtureRows = getFixtureRows(fixtureContent, aflFixtureUrl);
		
		int gameNo = 1;
        String date = "";

		for(WebElement fixtureRow : fixtureRows) {
            String rowData = fixtureRow.getAttribute(HTML_CLASS_STRING);

            if(rowData == null) {
                throw new AflFixtureException(aflFixtureUrl);
            } else if(rowData.contains("fixtures__date-header")) {
                date = fixtureRow.getText().trim();
            } else if(rowData.contains("fixtures__item")) {
                AflFixture fixture = setAflFixture(aflRound, gameNo, fixtureRow, date, aflFixtureUrl);

                loggerUtils.log("info", "Scraped fixture data: {}", fixture);

                games.add(fixture);

                gameNo++;
            } else if(rowData.contains("fixtures__tbc-date-wrapper")) {
                for(WebElement child : fixtureRow.findElements(By.xpath("./*"))) {
                    String childData = child.getAttribute(HTML_CLASS_STRING);
                    if(childData != null && childData.contains("fixtures__item")) {
                        AflFixture fixture = setAflFixture(aflRound, gameNo, child, date, aflFixtureUrl);
                        loggerUtils.log("info", "Scraped fixture data (TBC date): {}", fixture);
                        games.add(fixture);
                        gameNo++;
                    }
                }
            } else if(rowData.contains("fixtures__bye-fixtures") || rowData.contains("byes")) {
                loggerUtils.log("info", "Skipping bye round row");
            } else {
                loggerUtils.log("info", "Skipping unknown fixture row class: {}", rowData);
            }
        }

        return games;
    }

    AflFixture setAflFixture(int aflRound, int gameNo, WebElement fixtureRow, String date, String aflFixtureUrl) {
        AflFixture fixture = new AflFixture();
        fixture.setRound(aflRound);
        fixture.setGame(gameNo);

        List<WebElement> teams = fixtureRow.findElements(By.className("fixtures__match-team-name"));
        
        String homeTeam = teams.get(0).getAttribute("textContent");
        String awayTeam = teams.get(1).getAttribute("textContent");
        AflTeam homeTeamEntity = aflTeamService.getAflTeamByName(homeTeam);
        if (homeTeamEntity == null) throw new AflFixtureException(aflFixtureUrl, "Unknown home team: " + homeTeam);
        AflTeam awayTeamEntity = aflTeamService.getAflTeamByName(awayTeam);
        if (awayTeamEntity == null) throw new AflFixtureException(aflFixtureUrl, "Unknown away team: " + awayTeam);
        fixture.setHomeTeam(homeTeamEntity.getTeamId());
        fixture.setAwayTeam(awayTeamEntity.getTeamId());
        
        String ground = fixtureRow.findElement(By.className("fixtures__match-venue")).getText()
                            .split(",")[0].replaceAll("[^a-zA-Z0-9]", "");
        Map<String, String> groundData = globalsService.getGround(ground);
        fixture.setGround(groundData.get("ground"));
        fixture.setTimezone(groundData.get("timezone"));

        String timeWithTZ = fixtureRow.findElement(By.className("fixtures__status-label")).getText();
        if(timeWithTZ.equalsIgnoreCase("TBC") || timeWithTZ.equalsIgnoreCase("Postponed")) {
            loggerUtils.log("info", "Fixutre start time TBC: round={}, game={}", fixture.getRound(), fixture.getGame());
        } else {
            String time = timeWithTZ.split("\n")[0].toUpperCase();
            String tz = timeWithTZ.split("\n")[1].toUpperCase();
            String dateTimeString = date + " " + time + " " + currentYear;
            try {
                String scrappedTZ = tz.equalsIgnoreCase("GMT") ? "GMT" : defaultTimezone;
                ZonedDateTime localStart = LocalDateTime.parse(dateTimeString, formatter.withLocale(Locale.US)).atZone(ZoneId.of(scrappedTZ));
                fixture.setStartTime(localStart);
            } catch (Exception ex) {
                throw new AflFixtureException(aflFixtureUrl, ex);
            }
        }

        return fixture;
    }

    List<WebElement> getFixtureRows(WebElement fixtureContent, String aflFixtureUrl) {
        List<WebElement> contents = fixtureContent.findElements(By.className("wrapper"));
        List<WebElement> fixtureRows = null;
        for(WebElement content : contents) {
            String contentData = content.getAttribute(HTML_CLASS_STRING);
            if(contentData == null) {
                throw new AflFixtureException(aflFixtureUrl);
            } else if(contentData.equals("wrapper")) {
                fixtureRows = content.findElements(By.xpath("./*"));
            }
            if(fixtureRows != null && !fixtureRows.isEmpty()) {
                break;
            }
        }
        if(fixtureRows == null || fixtureRows.isEmpty()) {
            throw new AflFixtureException(aflFixtureUrl);
        }

        return fixtureRows;
    }

    public void report(List<AflFixture> fixtures) {
        for(AflFixture fixture: fixtures) {
            loggerUtils.log("info", "Fixture: {}", fixture);
        }
    }

    public static void main(String[] args) {

        int round = Integer.parseInt(args[0]);
        String fixtureUrl = args[1];

        AflFixtureHtmlHandler handler = new AflFixtureHtmlHandler();
        handler.configureLogging("AflFixtureDownloader");

        List<AflFixture> fixtures =  handler.execute(round, fixtureUrl);
        handler.report(fixtures);

        System.exit(0);
    }
}