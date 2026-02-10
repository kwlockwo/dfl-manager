package net.dflmngr.scheduler.generators;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.services.DflRoundInfoService;
import net.dflmngr.services.GlobalsService;
import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

@Component
public class EndRoundJobGenerator extends BaseJobGenerator {

	public EndRoundJobGenerator() {
		super("EndRoundJobGenerator");
	}

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;


	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.END_ROUND_JOB_GROUP);

		List<DflRoundInfo> dflRounds = dflRoundInfoService.findAll();

		for(DflRoundInfo dflRound : dflRounds) {
			loggerUtils.log("info", "Creating job entry for round={}, lockout={}", dflRound.getRound(), dflRound.getHardLockoutTime());
			createReportJobEntry(dflRound.getRound(), dflRound.getHardLockoutTime());
		}
	}

	@Override
	protected void closeServices() {
		if (dflRoundInfoService != null) {
		}
		if (globalsService != null) {
		}
	}
	
	private void createReportJobEntry(int round, ZonedDateTime time) throws Exception {
		ZonedDateTime runTime = time.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
		runTime = runTime.withHour(16).withMinute(0);

		JobScheduleHelper.scheduleJob(
			JobParameterConstants.END_ROUND_JOB_NAME,
			JobParameterConstants.END_ROUND_JOB_GROUP,
			JobParameterConstants.END_ROUND_JOB_CLASS,
			JobParameterConstants.PARAM_ROUND,
			round,
			runTime
		);
	}
	
	public static void main(String[] args) {		
		EndRoundJobGenerator testing = new EndRoundJobGenerator();
		testing.execute();
	}
}
