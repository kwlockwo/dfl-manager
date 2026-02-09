package net.dflmngr.scheduler.jobs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.TriggerFiredBundle;

class BaseJobTest {

	/**
	 * Concrete implementation of BaseJob for testing purposes.
	 */
	private static class TestJob extends BaseJob {
		private boolean executeJobCalled = false;
		private Exception exceptionToThrow = null;
		private JobDataMap receivedData = null;

		@Override
		protected void executeJob(JobDataMap data) throws Exception {
			executeJobCalled = true;
			receivedData = data;
			if (exceptionToThrow != null) {
				throw exceptionToThrow;
			}
		}

		public void setExceptionToThrow(Exception e) {
			this.exceptionToThrow = e;
		}

		public boolean wasExecuteJobCalled() {
			return executeJobCalled;
		}

		public JobDataMap getReceivedData() {
			return receivedData;
		}
	}

	/**
	 * Concrete implementation with custom job name for testing.
	 */
	private static class CustomNameJob extends BaseJob {
		@Override
		protected void executeJob(JobDataMap data) throws Exception {
			// No-op for testing
		}

		@Override
		protected String getJobName() {
			return "CustomJobName";
		}
	}

	@Test
	void execute_shouldCallExecuteJob() throws JobExecutionException {
		TestJob job = new TestJob();
		JobExecutionContext context = createContext();

		job.execute(context);

		assertTrue(job.wasExecuteJobCalled());
	}

	@Test
	void execute_shouldInitializeLoggerUtils() throws JobExecutionException {
		TestJob job = new TestJob();
		JobExecutionContext context = createContext();

		job.execute(context);

		assertNotNull(job.loggerUtils);
	}

	@Test
	void execute_shouldHandleExceptionGracefully() {
		TestJob job = new TestJob();
		job.setExceptionToThrow(new RuntimeException("Test exception"));
		JobExecutionContext context = createContext();

		// Should not throw exception - it's caught and logged
		assertDoesNotThrow(() -> job.execute(context));
		assertTrue(job.wasExecuteJobCalled());
	}

	@Test
	void execute_shouldPassJobDataMapToExecuteJob() throws JobExecutionException {
		TestJob job = new TestJob();

		JobDataMap expectedData = new JobDataMap();
		expectedData.put("test", "value");
		JobExecutionContext context = createContext(expectedData);

		job.execute(context);

		JobDataMap receivedData = job.getReceivedData();
		assertNotNull(receivedData);
		assertEquals("value", receivedData.getString("test"));
	}

	@Test
	void getJobName_shouldReturnSimpleClassName() {
		TestJob job = new TestJob();

		String jobName = job.getJobName();

		assertEquals("TestJob", jobName);
	}

	@Test
	void getJobName_shouldReturnCustomNameWhenOverridden() {
		CustomNameJob job = new CustomNameJob();

		String jobName = job.getJobName();

		assertEquals("CustomJobName", jobName);
	}

	@Test
	void getIntParam_shouldExtractIntegerValue() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("round", 5);

		int result = job.getIntParam(data, "round");

		assertEquals(5, result);
	}

	@Test
	void getBooleanParam_shouldExtractBooleanValue() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("isFinal", true);

		boolean result = job.getBooleanParam(data, "isFinal");

		assertTrue(result);
	}

	@Test
	void getBooleanParam_shouldReturnFalseForMissingKey() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();

		boolean result = job.getBooleanParam(data, "nonexistent");

		assertFalse(result);
	}

	@Test
	void getStringParam_shouldExtractStringValue() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("reportType", "summary");

		String result = job.getStringParam(data, "reportType");

		assertEquals("summary", result);
	}

	@Test
	void getStringParam_shouldReturnNullForMissingKey() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();

		String result = job.getStringParam(data, "nonexistent");

		assertNull(result);
	}

	@Test
	void execute_shouldExtractDataFromContext() throws JobExecutionException {
		TestJob job = new TestJob();

		JobDataMap data = new JobDataMap();
		data.put("round", 10);
		data.put("flag", true);
		JobExecutionContext context = createContext(data);

		job.execute(context);

		// Verify the job received the data
		JobDataMap receivedData = job.getReceivedData();
		assertNotNull(receivedData);
		assertEquals(10, receivedData.getInt("round"));
		assertTrue(receivedData.getBoolean("flag"));
	}

	@Test
	void execute_shouldHandleCheckedExceptions() {
		TestJob job = new TestJob();
		job.setExceptionToThrow(new Exception("Checked exception"));
		JobExecutionContext context = createContext();

		// Should catch and log the exception, not throw it
		assertDoesNotThrow(() -> job.execute(context));
		assertTrue(job.wasExecuteJobCalled());
	}

	@Test
	void execute_shouldHandleRuntimeExceptions() {
		TestJob job = new TestJob();
		job.setExceptionToThrow(new RuntimeException("Runtime exception"));
		JobExecutionContext context = createContext();

		// Should catch and log the exception, not throw it
		assertDoesNotThrow(() -> job.execute(context));
		assertTrue(job.wasExecuteJobCalled());
	}

	@Test
	void getIntParam_shouldHandleNegativeValues() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", -42);

		int result = job.getIntParam(data, "value");

		assertEquals(-42, result);
	}

	@Test
	void getIntParam_shouldHandleZero() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", 0);

		int result = job.getIntParam(data, "value");

		assertEquals(0, result);
	}

	@Test
	void getStringParam_shouldHandleEmptyString() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", "");

		String result = job.getStringParam(data, "value");

		assertEquals("", result);
	}

	@Test
	void getStringParam_shouldHandleWhitespace() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", "  spaces  ");

		String result = job.getStringParam(data, "value");

		assertEquals("  spaces  ", result);
	}

	@Test
	void getBooleanParam_shouldHandleTrueValue() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", true);

		boolean result = job.getBooleanParam(data, "value");

		assertTrue(result);
	}

	@Test
	void getBooleanParam_shouldHandleFalseValue() {
		TestJob job = new TestJob();
		JobDataMap data = new JobDataMap();
		data.put("value", false);

		boolean result = job.getBooleanParam(data, "value");

		assertFalse(result);
	}

	// Helper methods

	private JobExecutionContext createContext() {
		return createContext(new JobDataMap());
	}

	private JobExecutionContext createContext(JobDataMap dataMap) {
		try {
			JobDetailImpl jobDetail = new JobDetailImpl();
			jobDetail.setName("TestJob");
			jobDetail.setGroup("TestGroup");
			jobDetail.setJobClass(TestJob.class);
			jobDetail.setJobDataMap(dataMap);

			SimpleTriggerImpl trigger = new SimpleTriggerImpl();
			trigger.setName("TestTrigger");
			trigger.setGroup("TestGroup");

			TriggerFiredBundle bundle = new TriggerFiredBundle(
				jobDetail,
				trigger,
				null, // calendar
				false, // jobIsRecovering
				null, // fireTime
				null, // scheduledFireTime
				null, // prevFireTime
				null  // nextFireTime
			);

			return new JobExecutionContextImpl(null, bundle, null);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create JobExecutionContext", e);
		}
	}
}
