package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.StatsRoundPlayerStatsHandler;
import net.dflmngr.scheduler.JobParameterConstants;

public class StatsRoundJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);

		String logFile = "StatsRoundRound_R";

		StatsRoundPlayerStatsHandler statsRoundHandler = new StatsRoundPlayerStatsHandler();
		statsRoundHandler.configureLogging("online.name", "online-logger", logFile);

		loggerUtils.log("info", "Running {}", logFile);
		statsRoundHandler.execute(round);
		loggerUtils.log("info", "{} completed", logFile);
	}

}
