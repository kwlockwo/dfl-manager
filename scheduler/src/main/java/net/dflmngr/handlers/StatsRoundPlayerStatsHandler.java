package net.dflmngr.handlers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.service.AflFixtureService;
import org.springframework.stereotype.Service;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.utils.DflmngrUtils;


@Service
public class StatsRoundPlayerStatsHandler extends BaseHandler {

	private final DflRoundInfoService dflRoundInfoService;
	private final AflFixtureService aflFixtureService;
	private final GlobalsService globalsService;

	public StatsRoundPlayerStatsHandler(DflRoundInfoService dflRoundInfoService,
								AflFixtureService aflFixtureService,
								GlobalsService globalsService) {
		super("RawPlayerStatsHandler");
		this.dflRoundInfoService = dflRoundInfoService;
		this.aflFixtureService = aflFixtureService;
		this.globalsService = globalsService;
	}

	public void execute(int round) {

		try {
			ensureLoggingConfigured();

			loggerUtils.log("info", "Downloading stats round player stats for AFL round: {}", round);

			List<AflFixture> fixturesToProcess = aflFixtureService.getAflFixturesForRound(round);

			if(!fixturesToProcess.isEmpty()) {
				loggerUtils.log("info", "AFL games to download stats from: {}", fixturesToProcess);
				processFixtures(round, fixturesToProcess);

				List<AflFixture> updateFixtures = new ArrayList<>();
				for(AflFixture fixture : fixturesToProcess) {
					fixture.setEndTime(ZonedDateTime.now(ZoneId.of(DflmngrUtils.defaultTimezone)));
					fixture.setStatsDownloaded(true);
					updateFixtures.add(fixture);
				}
				if(!updateFixtures.isEmpty()) {
					loggerUtils.log("info", "AFL games final download: {}", updateFixtures);
					aflFixtureService.updateAll(updateFixtures, false);
				}
			}
			
			dflRoundInfoService.close();
			aflFixtureService.close();
			globalsService.close();

			loggerUtils.log("info", "Stats round player stats downaloded");

		} catch (Exception ex) {
			loggerUtils.logException("Error in ... ", ex);
		}
	}

	private void processFixtures(int round, List<AflFixture> fixturesToProcess) {

		String year = globalsService.getCurrentYear();
		String statsUrl = globalsService.getAflStatsUrl();

		for (AflFixture fixture : fixturesToProcess) {
			String homeTeam = fixture.getHomeTeam();
			String awayTeam = fixture.getAwayTeam();

			String roundStr = String.format("%02d", fixture.getRound());
			String gameStr = String.format("%02d", fixture.getGame());

			String fullStatsUrl = statsUrl + "/AFL" + year + roundStr + gameStr + "/playerstats";

			loggerUtils.log("info", "AFL stats URL: {}", fullStatsUrl);

			boolean includeHomeTeam = true;
			boolean includeAwayTeam = true;

			StatsDownloaderHandler handler = applicationContext.getBean(StatsDownloaderHandler.class);
			handler.setRound(round);
			handler.setStatsUrl(fullStatsUrl);
			handler.configureLogging("StatsRoundPlayerDownloader");
			handler.execute(homeTeam, awayTeam, includeHomeTeam, includeAwayTeam, "Finalized", true);
		}
	}

	// For internal testing
	public static void main(String[] args) {
		Options options = new Options();
		Option roundOpt  = Option.builder("r").argName("round").hasArg().desc("round to run on").type(Number.class).required().build();
		options.addOption(roundOpt);

		try {
			int round = 0;

			CommandLineParser parser = new DefaultParser();
			CommandLine cli = parser.parse(options, args);

			round = ((Number)cli.getParsedOptionValue("r")).intValue();

// Use Spring Boot ApplicationContext to get the handler bean
			org.springframework.context.ApplicationContext context =
				org.springframework.boot.SpringApplication.run(net.dflmngr.SchedulerApplication.class, args);
			StatsRoundPlayerStatsHandler testing = context.getBean(StatsRoundPlayerStatsHandler.class);
			testing.configureLogging("batch.name", "batch-logger", "StatsRoundPlayerStatsHandlerTesting");
			testing.execute(round);
			System.exit(0);

		} catch (ParseException ex) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "StatsRoundStatsHandler", options );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
