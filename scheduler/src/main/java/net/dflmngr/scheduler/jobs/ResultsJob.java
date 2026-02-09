package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.ResultsHandler;
import net.dflmngr.scheduler.JobParameterConstants;

public class ResultsJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);
		boolean isFinal = getBooleanParam(data, JobParameterConstants.PARAM_IS_FINAL);
		boolean ongoing = getBooleanParam(data, JobParameterConstants.PARAM_ONGOING);

		String logFile = "";
		boolean sendReport = false;
		boolean skipStats = false;

		if(ongoing) {
			logFile = "ResultsOngoing";
		} else {
			if(isFinal) {
				logFile = "ResultsRound_R" + round;
				sendReport = true;
			} else {
				logFile = "ProgressRound_R" + round;
			}
		}

		ResultsHandler resultsHandler = new ResultsHandler();
		resultsHandler.configureLogging(logFile);

		loggerUtils.log("info", "Running {}", logFile);
		resultsHandler.execute(round, isFinal, null, skipStats, sendReport);
		loggerUtils.log("info", "{} completed", logFile);
	}

}
