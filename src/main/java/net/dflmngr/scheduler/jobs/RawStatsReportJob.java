package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.reports.RawStatsReport;
import net.dflmngr.scheduler.JobParameterConstants;

public class RawStatsReportJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);
		boolean isFinal = getBooleanParam(data, JobParameterConstants.PARAM_IS_FINAL);

		RawStatsReport rawStatsReport = new RawStatsReport();

		loggerUtils.log("info", "Running rawStatsReport: round={}; isFinal={};", round, isFinal);
		rawStatsReport.execute(round, isFinal, null);
	}
}
