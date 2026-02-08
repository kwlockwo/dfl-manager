package net.dflmngr.scheduler.generators;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.scheduler.JobScheduler;
import net.dflmngr.utils.CronExpressionCreator;

public class EndRoundJobGenerator extends BaseJobGenerator {

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;

	private static final String JOB_NAME = "EndRoundJob";
	private static final String JOB_GROUP = "EndRound";
	private static final String JOB_CLASS = "net.dflmngr.scheduler.jobs.EndRoundJob";

	public EndRoundJobGenerator() {
		super("EndRoundJobGenerator");
		this.dflRoundInfoService = serviceFactory.createDflRoundInfoService();
		this.globalsService = serviceFactory.createGlobalsService();
	}

	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JOB_GROUP);

		List<DflRoundInfo> dflRounds = dflRoundInfoService.findAll();

		for(DflRoundInfo dflRound : dflRounds) {
			loggerUtils.log("info", "Creating job entry for round={}, lockout={}", dflRound.getRound(), dflRound.getHardLockoutTime());
			createReportJobEntry(dflRound.getRound(), dflRound.getHardLockoutTime());
		}
	}

	@Override
	protected void closeServices() {
		if (dflRoundInfoService != null) {
			dflRoundInfoService.close();
		}
		if (globalsService != null) {
			globalsService.close();
		}
	}
	
	private void createReportJobEntry(int round, ZonedDateTime time) throws Exception {

		CronExpressionCreator cronExpression = new CronExpressionCreator();

		ZonedDateTime runTime = time.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
		runTime = runTime.withHour(16).withMinute(0);

		cronExpression.setTime(runTime.format(DateTimeFormatter.ofPattern("hh:mm a")));
		cronExpression.setStartDate(runTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

		Map<String, Object> jobParams = new HashMap<>();
		jobParams.put("ROUND", round);

		JobScheduler.schedule(JOB_NAME, JOB_GROUP, JOB_CLASS, jobParams, cronExpression.getCronExpression(), false);
	}
	
	public static void main(String[] args) {		
		EndRoundJobGenerator testing = new EndRoundJobGenerator();
		testing.execute();
	}
}
