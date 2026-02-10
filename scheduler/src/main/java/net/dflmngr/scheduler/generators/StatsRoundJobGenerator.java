package net.dflmngr.scheduler.generators;

import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.services.AflFixtureService;
import net.dflmngr.services.GlobalsService;
import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

@Component
public class StatsRoundJobGenerator extends BaseJobGenerator {

	public StatsRoundJobGenerator() {
		super("StatsRoundJobGenerator");
	}

	private GlobalsService globalsService;
	private AflFixtureService aflFixtureService;


	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.STATS_ROUND_JOB_GROUP);

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
		}
		if (aflFixtureService != null) {
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

		JobScheduleHelper.scheduleJob(JobParameterConstants.STATS_ROUND_JOB_NAME, JobParameterConstants.STATS_ROUND_JOB_GROUP, JobParameterConstants.STATS_ROUND_JOB_CLASS, JobParameterConstants.PARAM_ROUND, round, time);
	}
	
	public static void main(String[] args) {		
		StatsRoundJobGenerator testing = new StatsRoundJobGenerator();
		testing.execute();
	}

}
