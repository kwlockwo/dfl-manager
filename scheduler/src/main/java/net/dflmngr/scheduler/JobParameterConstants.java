package net.dflmngr.scheduler;

/**
 * Constants for Quartz job metadata and parameters.
 * Centralizes all job names, groups, classes, and parameter keys to prevent typos and duplication.
 */
public final class JobParameterConstants {

	// Prevent instantiation
	private JobParameterConstants() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	// Job metadata for EmailSelectionsJob
	public static final String EMAIL_SELECTIONS_JOB_NAME = "EmailSelections";
	public static final String EMAIL_SELECTIONS_JOB_GROUP = "Selections";
	public static final String EMAIL_SELECTIONS_JOB_CLASS = "net.dflmngr.scheduler.jobs.EmailSelectionsJob";

	// Job metadata for EndRoundJob
	public static final String END_ROUND_JOB_NAME = "EndRoundJob";
	public static final String END_ROUND_JOB_GROUP = "EndRound";
	public static final String END_ROUND_JOB_CLASS = "net.dflmngr.scheduler.jobs.EndRoundJob";

	// Job metadata for InsAndOutsReportJob
	public static final String INS_OUTS_REPORT_JOB_NAME = "InsOutsReport";
	public static final String INS_OUTS_REPORT_JOB_GROUP = "InsOutsReports";
	public static final String INS_OUTS_REPORT_JOB_CLASS = "net.dflmngr.scheduler.jobs.InsAndOutsReportJob";

	// Job metadata for RawStatsReportJob
	public static final String RAW_STATS_REPORT_JOB_NAME = "RawStatsReport";
	public static final String RAW_STATS_REPORT_JOB_GROUP = "StatsReports";
	public static final String RAW_STATS_REPORT_JOB_CLASS = "net.dflmngr.scheduler.jobs.RawStatsReportJob";

	// Job metadata for ResultsJob
	public static final String RESULTS_JOB_NAME_ROUND_PROGRESS = "RoundProgress";
	public static final String RESULTS_JOB_NAME_RESULTS = "Results";
	public static final String RESULTS_JOB_NAME_ONGOING_RESULTS = "OngoingResults";
	public static final String RESULTS_JOB_GROUP = "Results";
	public static final String RESULTS_JOB_CLASS = "net.dflmngr.scheduler.jobs.ResultsJob";

	// Job metadata for StartRoundJob
	public static final String START_ROUND_JOB_NAME = "StartRoundJob";
	public static final String START_ROUND_JOB_GROUP = "StartRound";
	public static final String START_ROUND_JOB_CLASS = "net.dflmngr.scheduler.jobs.StartRoundJob";

	// Job metadata for StatsRoundJob
	public static final String STATS_ROUND_JOB_NAME = "StatsRoundPlayerStats";
	public static final String STATS_ROUND_JOB_GROUP = "StatsRound";
	public static final String STATS_ROUND_JOB_CLASS = "net.dflmngr.scheduler.jobs.StatsRoundJob";

	// Job parameter keys
	public static final String PARAM_ROUND = "ROUND";
	public static final String PARAM_IS_FINAL = "IS_FINAL";
	public static final String PARAM_ONGOING = "ONGOING";
	public static final String PARAM_REPORT_TYPE = "REPORT_TYPE";
}
