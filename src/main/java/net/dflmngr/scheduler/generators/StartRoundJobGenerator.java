package net.dflmngr.scheduler.generators;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

import net.dflmngr.model.entity.DflRoundEarlyGames;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

public class StartRoundJobGenerator extends BaseJobGenerator {

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;

	private static final String JOB_NAME = "StartRoundJob";
	private static final String JOB_GROUP = "StartRound";
	private static final String JOB_CLASS = "net.dflmngr.scheduler.jobs.StartRoundJob";

	public StartRoundJobGenerator() {
		super("StartRoundJobGenerator");
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
			dflRoundInfoService.close();
		}
		if (globalsService != null) {
			globalsService.close();
		}
	}
	
	private void createReportJobEntry(int round, ZonedDateTime lockoutTime) throws Exception {

		ZonedDateTime time = lockoutTime.plusMinutes(10);

		JobScheduleHelper.scheduleJob(JOB_NAME, JOB_GROUP, JOB_CLASS, "ROUND", round, time);
	}

	private void createEarlyGameJobEntry(int round, List<DflRoundEarlyGames> earlyGames) throws Exception {

		Comparator<DflRoundEarlyGames> comparator = Comparator.comparingInt(DflRoundEarlyGames::getRound).thenComparingInt(DflRoundEarlyGames::getAflGame);
		earlyGames.sort(comparator);

		ZonedDateTime time = earlyGames.get(0).getStartTime().minusMinutes(30);

		JobScheduleHelper.scheduleJob(JOB_NAME, JOB_GROUP, JOB_CLASS, "ROUND", round, time);
	}
	
	public static void main(String[] args) {		
		StartRoundJobGenerator testing = new StartRoundJobGenerator();
		testing.execute();
	}
}
