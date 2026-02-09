package net.dflmngr.scheduler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import net.dflmngr.utils.CronExpressionCreator;

/**
 * Helper class for scheduling Quartz jobs.
 * Consolidates common scheduling operations and cron expression creation.
 */
public class JobScheduleHelper {

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	/**
	 * Schedule a job with the given parameters at the specified time.
	 *
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @param jobClass the fully qualified job class name
	 * @param jobParams the job parameters map
	 * @param scheduledTime when the job should run
	 * @throws Exception if scheduling fails
	 */
	public static void scheduleJob(String jobName, String jobGroup, String jobClass,
	                                Map<String, Object> jobParams, ZonedDateTime scheduledTime) throws Exception {
		String cronExpression = createCronExpression(scheduledTime);
		JobScheduler.schedule(jobName, jobGroup, jobClass, jobParams, cronExpression, false);
	}

	/**
	 * Schedule a job with a single parameter at the specified time.
	 * Convenience method for the common case of scheduling with just a round number.
	 *
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @param jobClass the fully qualified job class name
	 * @param paramKey the parameter key
	 * @param paramValue the parameter value
	 * @param scheduledTime when the job should run
	 * @throws Exception if scheduling fails
	 */
	public static void scheduleJob(String jobName, String jobGroup, String jobClass,
	                                String paramKey, Object paramValue, ZonedDateTime scheduledTime) throws Exception {
		Map<String, Object> jobParams = new HashMap<>();
		jobParams.put(paramKey, paramValue);
		scheduleJob(jobName, jobGroup, jobClass, jobParams, scheduledTime);
	}

	/**
	 * Create a cron expression for the given time.
	 * Uses consistent date/time formatting across all job generators.
	 *
	 * @param scheduledTime the time to schedule
	 * @return the cron expression string
	 */
	public static String createCronExpression(ZonedDateTime scheduledTime) {
		CronExpressionCreator cronExpression = new CronExpressionCreator();
		cronExpression.setTime(scheduledTime.format(TIME_FORMATTER));
		cronExpression.setStartDate(scheduledTime.format(DATE_FORMATTER));
		return cronExpression.getCronExpression();
	}

	/**
	 * Create a job parameters map with a single entry.
	 * Convenience method for common single-parameter case.
	 *
	 * @param key the parameter key
	 * @param value the parameter value
	 * @return the parameters map
	 */
	public static Map<String, Object> createJobParams(String key, Object value) {
		Map<String, Object> params = new HashMap<>();
		params.put(key, value);
		return params;
	}
}
