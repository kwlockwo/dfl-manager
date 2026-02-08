package net.dflmngr.scheduler.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.PersistJobDataAfterExecution;

import net.dflmngr.reports.InsAndOutsReport;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class InsAndOutsReportJob extends BaseJob {

	public static String ROUND = "ROUND";
	public static String REPORT_TYPE = "REPORT_TYPE";

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		int round = getIntParam(data, ROUND);
		String reportType = getStringParam(data, REPORT_TYPE);

		InsAndOutsReport insAndOutsReport = new InsAndOutsReport();

		loggerUtils.log("info", "Running insAndOutsReport: round={}; reportType={};", round, reportType);
		insAndOutsReport.execute(round, reportType, null);
	}
}
