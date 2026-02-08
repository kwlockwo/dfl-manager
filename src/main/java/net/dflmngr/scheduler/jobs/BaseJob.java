package net.dflmngr.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import net.dflmngr.logging.LoggingUtils;

/**
 * Base class for all Quartz jobs.
 * Provides common functionality for logging, exception handling, and parameter extraction.
 */
public abstract class BaseJob implements Job {

	protected LoggingUtils loggerUtils;

	@Override
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		loggerUtils = new LoggingUtils("Scheduler");

		try {
			loggerUtils.log("info", "{} starting ...", getJobName());

			JobDataMap data = context.getJobDetail().getJobDataMap();

			// Subclass implements specific job logic
			executeJob(data);

			loggerUtils.log("info", "{} completed", getJobName());
		} catch (Exception ex) {
			loggerUtils.logException("Error in " + getJobName(), ex);
		}
	}

	/**
	 * Subclasses implement this method to perform their specific job logic.
	 * The JobDataMap has already been extracted from the context.
	 *
	 * @param data the JobDataMap containing job parameters
	 * @throws Exception if job execution fails
	 */
	protected abstract void executeJob(JobDataMap data) throws Exception;

	/**
	 * Returns the name of this job for logging purposes.
	 * Defaults to the simple class name.
	 */
	protected String getJobName() {
		return this.getClass().getSimpleName();
	}

	/**
	 * Utility method to safely extract an int parameter from JobDataMap.
	 *
	 * @param data the JobDataMap
	 * @param key the parameter key
	 * @return the int value
	 */
	protected int getIntParam(JobDataMap data, String key) {
		return data.getInt(key);
	}

	/**
	 * Utility method to safely extract a boolean parameter from JobDataMap.
	 *
	 * @param data the JobDataMap
	 * @param key the parameter key
	 * @return the boolean value
	 */
	protected boolean getBooleanParam(JobDataMap data, String key) {
		return data.getBoolean(key);
	}

	/**
	 * Utility method to safely extract a String parameter from JobDataMap.
	 *
	 * @param data the JobDataMap
	 * @param key the parameter key
	 * @return the String value
	 */
	protected String getStringParam(JobDataMap data, String key) {
		return data.getString(key);
	}
}
