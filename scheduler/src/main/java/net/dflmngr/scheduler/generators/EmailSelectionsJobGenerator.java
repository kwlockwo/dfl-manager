package net.dflmngr.scheduler.generators;

import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduler;

public class EmailSelectionsJobGenerator extends BaseJobGenerator {

	public EmailSelectionsJobGenerator() {
		super("EmailSelectionsJobGenerator");
	}

	public EmailSelectionsJobGenerator() {
		super("EmailSelectionsJobGenerator");
	}

	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.EMAIL_SELECTIONS_JOB_GROUP);
		JobScheduler.schedule(
			JobParameterConstants.EMAIL_SELECTIONS_JOB_NAME,
			JobParameterConstants.EMAIL_SELECTIONS_JOB_GROUP,
			JobParameterConstants.EMAIL_SELECTIONS_JOB_CLASS,
			null,
			"0 0/15 * 1/1 * ? *",
			false
		);
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
