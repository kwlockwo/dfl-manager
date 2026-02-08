package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.reports.RawStatsReport;

public class RawStatsReportJob extends BaseJob {

	public static String ROUND = "ROUND";
	public static String IS_FINAL = "IS_FINAL";

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, ROUND);
		boolean isFinal = getBooleanParam(data, IS_FINAL);

		RawStatsReport rawStatsReport = new RawStatsReport();

		loggerUtils.log("info", "Running rawStatsReport: round={}; isFinal={};", round, isFinal);
		rawStatsReport.execute(round, isFinal, null);
	}
}
