package net.dflmngr.scheduler.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.PersistJobDataAfterExecution;

import net.dflmngr.reports.InsAndOutsReport;
import net.dflmngr.scheduler.JobParameterConstants;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InsAndOutsReportJob extends BaseJob {

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, JobParameterConstants.PARAM_ROUND);
		String reportType = getStringParam(data, JobParameterConstants.PARAM_REPORT_TYPE);

		InsAndOutsReport insAndOutsReport = new InsAndOutsReport();

		loggerUtils.log("info", "Running insAndOutsReport: round={}; reportType={};", round, reportType);
		insAndOutsReport.execute(round, reportType, null);
	}
}
