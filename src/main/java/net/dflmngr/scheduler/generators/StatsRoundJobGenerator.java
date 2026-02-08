package net.dflmngr.scheduler.generators;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

public class StatsRoundJobGenerator extends BaseJobGenerator {

	private static final String JOB_NAME = "StatsRoundPlayerStats";
	private static final String JOB_GROUP = "StatsRound";
	private static final String JOB_CLASS = "net.dflmngr.scheduler.jobs.StatsRoundJob";

	private GlobalsService globalsService;
	private AflFixtureService aflFixtureService;

	public StatsRoundJobGenerator() {
		super("StatsRoundJobGenerator");
		this.globalsService = serviceFactory.createGlobalsService();
		this.aflFixtureService = serviceFactory.createAflFixtureService();
	}

	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JOB_GROUP);

		List<Integer> statsRounds = globalsService.getStatRounds();

		for(int aflRound : statsRounds) {
			loggerUtils.log("info", "Create stats round job for AFL round={};", aflRound);
			List<AflFixture> aflFixtures = aflFixtureService.getAflFixturesForRound(aflRound);
			processFixtures(aflRound, aflFixtures);
		}
	}

	@Override
	protected void closeServices() {
		if (globalsService != null) {
			globalsService.close();
		}
		if (aflFixtureService != null) {
			aflFixtureService.close();
		}
	}
	
	private void processFixtures(int aflRound, List<AflFixture> aflFixtures) throws Exception {
		Collections.sort(aflFixtures, Collections.reverseOrder());

		ZonedDateTime lastGameStart = aflFixtures.get(0).getStartTime();
		loggerUtils.log("info", "Last AFL game starting time={}", lastGameStart);

		createSchedule(aflRound, aflFixtures.get(0).getStartTime());
	}
			
	private void createSchedule(int aflRound, ZonedDateTime time) throws Exception {
		time = time.withHour(23);
		time = time.withMinute(0);
		scheduleJob(aflRound, time);	
	}
	
	private void scheduleJob(int round, ZonedDateTime time) throws Exception {
		loggerUtils.log("info", "Scheduling StatsRoundJob");

		JobScheduleHelper.scheduleJob(JOB_NAME, JOB_GROUP, JOB_CLASS, "ROUND", round, time);
	}
	
	public static void main(String[] args) {		
		StatsRoundJobGenerator testing = new StatsRoundJobGenerator();
		testing.execute();
	}

}
