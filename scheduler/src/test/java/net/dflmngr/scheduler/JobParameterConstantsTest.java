package net.dflmngr.scheduler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JobParameterConstantsTest {

	@Test
	void constructor_shouldThrowException() throws Exception {
		// Use reflection to invoke private constructor
		var constructor = JobParameterConstants.class.getDeclaredConstructor();
		constructor.setAccessible(true);

		try {
			constructor.newInstance();
			fail("Expected UnsupportedOperationException to be thrown");
		} catch (java.lang.reflect.InvocationTargetException e) {
			// Unwrap the exception thrown by the constructor
			assertTrue(e.getCause() instanceof UnsupportedOperationException);
			assertEquals("This is a utility class and cannot be instantiated", e.getCause().getMessage());
		}
	}

	@Test
	void emailSelectionsJobConstants_shouldHaveCorrectValues() {
		assertEquals("EmailSelections", JobParameterConstants.EMAIL_SELECTIONS_JOB_NAME);
		assertEquals("Selections", JobParameterConstants.EMAIL_SELECTIONS_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.EmailSelectionsJob", JobParameterConstants.EMAIL_SELECTIONS_JOB_CLASS);
	}

	@Test
	void endRoundJobConstants_shouldHaveCorrectValues() {
		assertEquals("EndRoundJob", JobParameterConstants.END_ROUND_JOB_NAME);
		assertEquals("EndRound", JobParameterConstants.END_ROUND_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.EndRoundJob", JobParameterConstants.END_ROUND_JOB_CLASS);
	}

	@Test
	void insAndOutsReportJobConstants_shouldHaveCorrectValues() {
		assertEquals("InsOutsReport", JobParameterConstants.INS_OUTS_REPORT_JOB_NAME);
		assertEquals("InsOutsReports", JobParameterConstants.INS_OUTS_REPORT_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.InsAndOutsReportJob", JobParameterConstants.INS_OUTS_REPORT_JOB_CLASS);
	}

	@Test
	void rawStatsReportJobConstants_shouldHaveCorrectValues() {
		assertEquals("RawStatsReport", JobParameterConstants.RAW_STATS_REPORT_JOB_NAME);
		assertEquals("StatsReports", JobParameterConstants.RAW_STATS_REPORT_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.RawStatsReportJob", JobParameterConstants.RAW_STATS_REPORT_JOB_CLASS);
	}

	@Test
	void resultsJobConstants_shouldHaveCorrectValues() {
		assertEquals("RoundProgress", JobParameterConstants.RESULTS_JOB_NAME_ROUND_PROGRESS);
		assertEquals("Results", JobParameterConstants.RESULTS_JOB_NAME_RESULTS);
		assertEquals("OngoingResults", JobParameterConstants.RESULTS_JOB_NAME_ONGOING_RESULTS);
		assertEquals("Results", JobParameterConstants.RESULTS_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.ResultsJob", JobParameterConstants.RESULTS_JOB_CLASS);
	}

	@Test
	void startRoundJobConstants_shouldHaveCorrectValues() {
		assertEquals("StartRoundJob", JobParameterConstants.START_ROUND_JOB_NAME);
		assertEquals("StartRound", JobParameterConstants.START_ROUND_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.StartRoundJob", JobParameterConstants.START_ROUND_JOB_CLASS);
	}

	@Test
	void statsRoundJobConstants_shouldHaveCorrectValues() {
		assertEquals("StatsRoundPlayerStats", JobParameterConstants.STATS_ROUND_JOB_NAME);
		assertEquals("StatsRound", JobParameterConstants.STATS_ROUND_JOB_GROUP);
		assertEquals("net.dflmngr.scheduler.jobs.StatsRoundJob", JobParameterConstants.STATS_ROUND_JOB_CLASS);
	}

	@Test
	void parameterConstants_shouldHaveCorrectValues() {
		assertEquals("ROUND", JobParameterConstants.PARAM_ROUND);
		assertEquals("IS_FINAL", JobParameterConstants.PARAM_IS_FINAL);
		assertEquals("ONGOING", JobParameterConstants.PARAM_ONGOING);
		assertEquals("REPORT_TYPE", JobParameterConstants.PARAM_REPORT_TYPE);
	}

	@Test
	void jobGroupConstants_shouldHaveExpectedValues() {
		// Verify each job group
		assertEquals("Selections", JobParameterConstants.EMAIL_SELECTIONS_JOB_GROUP);
		assertEquals("EndRound", JobParameterConstants.END_ROUND_JOB_GROUP);
		assertEquals("InsOutsReports", JobParameterConstants.INS_OUTS_REPORT_JOB_GROUP);
		assertEquals("StatsReports", JobParameterConstants.RAW_STATS_REPORT_JOB_GROUP);
		assertEquals("Results", JobParameterConstants.RESULTS_JOB_GROUP);
		assertEquals("StartRound", JobParameterConstants.START_ROUND_JOB_GROUP);
		assertEquals("StatsRound", JobParameterConstants.STATS_ROUND_JOB_GROUP);
	}

	@Test
	void jobClassConstants_shouldHaveCorrectPackage() {
		// All job classes should be in net.dflmngr.scheduler.jobs package
		assertTrue(JobParameterConstants.EMAIL_SELECTIONS_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.END_ROUND_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.INS_OUTS_REPORT_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.RAW_STATS_REPORT_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.RESULTS_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.START_ROUND_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
		assertTrue(JobParameterConstants.STATS_ROUND_JOB_CLASS.startsWith("net.dflmngr.scheduler.jobs."));
	}

	@Test
	void jobNameConstants_shouldBeUnique() {
		// All job names should be unique (including Results job variants)
		String[] jobNames = {
			JobParameterConstants.EMAIL_SELECTIONS_JOB_NAME,
			JobParameterConstants.END_ROUND_JOB_NAME,
			JobParameterConstants.INS_OUTS_REPORT_JOB_NAME,
			JobParameterConstants.RAW_STATS_REPORT_JOB_NAME,
			JobParameterConstants.RESULTS_JOB_NAME_ROUND_PROGRESS,
			JobParameterConstants.RESULTS_JOB_NAME_RESULTS,
			JobParameterConstants.RESULTS_JOB_NAME_ONGOING_RESULTS,
			JobParameterConstants.START_ROUND_JOB_NAME,
			JobParameterConstants.STATS_ROUND_JOB_NAME
		};

		// Check each name is unique
		for (int i = 0; i < jobNames.length; i++) {
			for (int j = i + 1; j < jobNames.length; j++) {
				assertNotEquals(jobNames[i], jobNames[j],
					"Job names should be unique: " + jobNames[i] + " vs " + jobNames[j]);
			}
		}
	}
}
