package net.dflmngr.scheduler.jobs;

import org.quartz.JobDataMap;

import net.dflmngr.handlers.EmailSelectionsHandler;

public class EmailSelectionsJob extends BaseJob {

	String defaultMdcKey = "online.name";
	String defaultLoggerName = "online-logger";
	String defaultLogfile = "Selections";

	@Override
	protected void executeJob(JobDataMap data) throws Exception {
		EmailSelectionsHandler emailSelectionsHandler = applicationContext.getBean(EmailSelectionsHandler.class);
		emailSelectionsHandler.configureLogging(defaultMdcKey, defaultLoggerName, defaultLogfile);
		emailSelectionsHandler.execute();
	}
}
