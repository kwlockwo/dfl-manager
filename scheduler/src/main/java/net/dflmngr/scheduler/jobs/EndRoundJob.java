package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.EndRoundHandler;
import net.dflmngr.scheduler.JobParameterConstants;

public class EndRoundJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);

		EndRoundHandler endRound = applicationContext.getBean(EndRoundHandler.class);
		endRound.configureLogging("online.name", "online-logger", ("EndRound_R"+round));

		loggerUtils.log("info", "Running EndRound: round={};", round);
		endRound.execute(round, null);
	}
}
