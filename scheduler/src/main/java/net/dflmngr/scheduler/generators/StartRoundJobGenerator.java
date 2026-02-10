package net.dflmngr.scheduler.generators;

import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import net.dflmngr.model.entity.DflRoundEarlyGames;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.services.DflRoundInfoService;
import net.dflmngr.services.GlobalsService;
import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

@Component
public class StartRoundJobGenerator extends BaseJobGenerator {

	public StartRoundJobGenerator() {
		super("StartRoundJobGenerator");
	}

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;


	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.START_ROUND_JOB_GROUP);

		List<DflRoundInfo> dflRounds = dflRoundInfoService.findAll();

		for(DflRoundInfo dflRound : dflRounds) {
			loggerUtils.log("info", "Creating job entry for round={}, lockout={}", dflRound.getRound(), dflRound.getHardLockoutTime());
			createReportJobEntry(dflRound.getRound(), dflRound.getHardLockoutTime());

			List<DflRoundEarlyGames> earlyGames = dflRound.getEarlyGames();

			if(earlyGames != null && !earlyGames.isEmpty()) {
				loggerUtils.log("info", "Creating job entry for earlyGames round={}, earlyGames={}", dflRound.getRound(), earlyGames);
				createEarlyGameJobEntry(dflRound.getRound(), earlyGames);
			} else {
				loggerUtils.log("info", "No early games for round={}", dflRound.getRound());
			}
		}
	}

	@Override
	protected void closeServices() {
		if (dflRoundInfoService != null) {
		}
		if (globalsService != null) {
		}
	}
	
	private void createReportJobEntry(int round, ZonedDateTime lockoutTime) throws Exception {

		ZonedDateTime time = lockoutTime.plusMinutes(10);

		JobScheduleHelper.scheduleJob(
			JobParameterConstants.START_ROUND_JOB_NAME,
			JobParameterConstants.START_ROUND_JOB_GROUP,
			JobParameterConstants.START_ROUND_JOB_CLASS,
			JobParameterConstants.PARAM_ROUND,
			round,
			time
		);
	}

	private void createEarlyGameJobEntry(int round, List<DflRoundEarlyGames> earlyGames) throws Exception {

		Comparator<DflRoundEarlyGames> comparator = Comparator.comparingInt(DflRoundEarlyGames::getRound).thenComparingInt(DflRoundEarlyGames::getAflGame);
		earlyGames.sort(comparator);

		ZonedDateTime time = earlyGames.get(0).getStartTime().minusMinutes(30);

		JobScheduleHelper.scheduleJob(
			JobParameterConstants.START_ROUND_JOB_NAME,
			JobParameterConstants.START_ROUND_JOB_GROUP,
			JobParameterConstants.START_ROUND_JOB_CLASS,
			JobParameterConstants.PARAM_ROUND,
			round,
			time
		);
	}
	
	public static void main(String[] args) {		
		StartRoundJobGenerator testing = new StartRoundJobGenerator();
		testing.execute();
	}
}
