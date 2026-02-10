package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import net.dflmngr.model.entity.RawPlayerStats;
import net.dflmngr.model.entity.StatsRoundPlayerStats;
import net.dflmngr.model.service.GlobalsService;
import org.springframework.stereotype.Service;
import net.dflmngr.model.service.RawPlayerStatsService;
import net.dflmngr.model.service.StatsRoundPlayerStatsService;


@Service
public class StatsDownloaderHandler extends BaseHandler {

	private final RawPlayerStatsService rawPlayerStatsService;
	private final StatsRoundPlayerStatsService statsRoundPlayerStatsService;
	private final GlobalsService globalsService;

	int round;
	String statsUrl;

	public StatsDownloaderHandler(RawPlayerStatsService rawPlayerStatsService,
								   StatsRoundPlayerStatsService statsRoundPlayerStatsService,
								   GlobalsService globalsService) {
		super("RoundProgress");
		this.rawPlayerStatsService = rawPlayerStatsService;
		this.statsRoundPlayerStatsService = statsRoundPlayerStatsService;
		this.globalsService = globalsService;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public void setStatsUrl(String statsUrl) {
		this.statsUrl = statsUrl;
	}

	public void configureLogging(String logfile) {
		configureLogging(defaultMdcKey, defaultLoggerName, logfile);
	}

	public void execute(String homeTeam, String awayTeam, boolean includeHomeTeam, boolean includeAwayTeam, String scrapingStatus, boolean isStatsRound) {

		try {
			ensureLoggingConfigured();

			if(isStatsRound) {
				loggerUtils.log("info", "Running for Stats round: AFL round={}", round);
			}

			loggerUtils.log("info", "Downloading AFL stats: round={}, homeTeam={} awayTeam={} url={}", round, homeTeam, awayTeam, statsUrl);

			List<RawPlayerStats> playerStats = null;
			boolean statsDownloaded = false;
			for(int i = 0; i < 5; i++) {
				loggerUtils.log("info", "Attempt {}", i);
				try {
					StatsHtmlHandler htmlHandler = applicationContext.getBean(StatsHtmlHandler.class);
					htmlHandler.configureLogging(logfile);

					playerStats = htmlHandler.execute(round, homeTeam, awayTeam, statsUrl, includeHomeTeam, includeAwayTeam, scrapingStatus);

					loggerUtils.log("info", "Player stats count: {}", playerStats.size());
					if(includeHomeTeam && includeAwayTeam) {
						if(playerStats.size() >= 44) {
							statsDownloaded = true;
							break;
						}
					} else {
						if(playerStats.size() >= 22) {
							statsDownloaded = true;
							break;
						}
					}
				} catch (Exception ex) {
					loggerUtils.log("info", "Exception caught downloading stats will try again");
					loggerUtils.log("info", "Exception stacktrace={}", ExceptionUtils.getStackTrace(ex));
				}
			}
			if(statsDownloaded && playerStats != null) {
				loggerUtils.log("info", "Saving player stats to database");

				if(isStatsRound) {
					List<StatsRoundPlayerStats> statRoundPlayerStats = new ArrayList<>();
					for(RawPlayerStats pStats : playerStats) {
						StatsRoundPlayerStats statRoundpStats = new StatsRoundPlayerStats();
						BeanUtils.copyProperties(statRoundpStats, pStats);
						statRoundPlayerStats.add(statRoundpStats);
					}
					
					if(includeHomeTeam) {
						statsRoundPlayerStatsService.removeStatsForRoundAndTeam(round, homeTeam);
					}
					if(includeAwayTeam) {
						statsRoundPlayerStatsService.removeStatsForRoundAndTeam(round, awayTeam);
					}
					statsRoundPlayerStatsService.insertAll(statRoundPlayerStats, false);
				} else {
					if(includeHomeTeam) {
						rawPlayerStatsService.removeStatsForRoundAndTeam(round, homeTeam);
					}
					if(includeAwayTeam) {
						rawPlayerStatsService.removeStatsForRoundAndTeam(round, awayTeam);
					}
					rawPlayerStatsService.insertAll(playerStats, false);
				}

				loggerUtils.log("info", "Player stats saved");
			} else {
				loggerUtils.log("info", "Player stats were not downloaded");
			}
		} catch (Exception ex) {
			loggerUtils.logException("Error in ... ", ex);
		} finally {
			rawPlayerStatsService.close();
			statsRoundPlayerStatsService.close();
			globalsService.close();
		}
	}

	// For internal testing
	public static void main(String[] args) {

		int round = Integer.parseInt(args[0]);
		String homeTeam = args[1];
		String awayTeam = args[2];
		String statsUrl = args[3];
		boolean includeHomeTeam = Boolean.parseBoolean(args[4]);
		boolean includeAwayTeam = Boolean.parseBoolean(args[5]);
		String scrapingStatus = args[6];
		boolean isStatsRound = Boolean.parseBoolean(args[7]);

		org.springframework.context.ApplicationContext context =
			org.springframework.boot.SpringApplication.run(net.dflmngr.SchedulerApplication.class, args);
		StatsDownloaderHandler handler = context.getBean(StatsDownloaderHandler.class);
		handler.setRound(round);
		handler.setStatsUrl(statsUrl);
		handler.configureLogging("RawPlayerDownloader");
		handler.execute(homeTeam, awayTeam, includeHomeTeam, includeAwayTeam, scrapingStatus, isStatsRound);

		System.exit(0);
	}
}
