package net.dflmngr.scheduler.generators;

import net.dflmngr.scheduler.JobScheduler;

public class EmailSelectionsJobGenerator extends BaseJobGenerator {

	private static final String JOB_NAME = "EmailSelections";
	private static final String JOB_GROUP = "Selections";
	private static final String JOB_CLASS = "net.dflmngr.scheduler.jobs.EmailSelectionsJob";

	public EmailSelectionsJobGenerator() {
		super("EmailSelectionsJobGenerator");
	}

	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JOB_GROUP);
		JobScheduler.schedule(JOB_NAME, JOB_GROUP, JOB_CLASS, null, "0 0/15 * 1/1 * ? *", false);
	}

	@Override
	protected void closeServices() {
		// No services to close
	}

	// For internal testing
	public static void main(String[] args) {
		EmailSelectionsJobGenerator testing = new EmailSelectionsJobGenerator();
		testing.execute();
	}
}
