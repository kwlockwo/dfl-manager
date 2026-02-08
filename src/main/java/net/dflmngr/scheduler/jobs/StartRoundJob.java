package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.StartRoundHandler;
import net.dflmngr.scheduler.JobParameterConstants;

public class StartRoundJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);

		StartRoundHandler startRound = new StartRoundHandler();
		startRound.configureLogging("online.name", "online-logger", ("StartRound_R"+round));

		loggerUtils.log("info", "Running StartRound: round={};", round);
		startRound.execute(round, null, false);
	}
}
