package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.ResultsHandler;

public class ResultsJob extends BaseJob {
	private static final String ROUND = "ROUND";
	private static final String IS_FINAL = "IS_FINAL";
	private static final String ONGOING = "ONGOING";

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, ROUND);
		boolean isFinal = getBooleanParam(data, IS_FINAL);
		boolean ongoing = getBooleanParam(data, ONGOING);

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
