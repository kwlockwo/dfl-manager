package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.EndRoundHandler;

public class EndRoundJob extends BaseJob {

	public static String ROUND = "ROUND";

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, ROUND);

		EndRoundHandler endRound = new EndRoundHandler();
		endRound.configureLogging("online.name", "online-logger", ("EndRound_R"+round));

		loggerUtils.log("info", "Running EndRound: round={};", round);
		endRound.execute(round, null);
	}
}
