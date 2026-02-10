package net.dflmngr.scheduler.generators;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.AflFixture;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.entity.DflRoundMapping;
import net.dflmngr.model.entity.keys.AflFixturePK;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;

public class RawStatsReportJobGenerator extends BaseJobGenerator {

	public RawStatsReportJobGenerator() {
		super("RawStatsReportJobGenerator");
	}

	private DflRoundInfoService dflRoundInfoService;
	private AflFixtureService aflFixtureService;


	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.RAW_STATS_REPORT_JOB_GROUP);

		List<DflRoundInfo> dflSeason = dflRoundInfoService.findAll();

		for(DflRoundInfo roundInfo : dflSeason) {
			List<DflRoundMapping> roundMapping = roundInfo.getRoundMapping();

			List<AflFixture> dflAflGames = new ArrayList<>();
			for(DflRoundMapping mapping : roundMapping) {
				loggerUtils.log("info", "Finding AFL games for: DFL round={}; AFL round={};", roundInfo.getRound(), mapping.getAflRound());
				if(mapping.getAflGame() == 0) {
					loggerUtils.log("info", "No AFL game mapping adding all games");
					dflAflGames.addAll(aflFixtureService.getAflFixturesForRound(mapping.getAflRound()));
				} else {
					loggerUtils.log("info", "Bye round adding: AFL round={}; game={};", mapping.getAflRound(), mapping.getAflGame());
					AflFixturePK aflFixturePK = new AflFixturePK();
					aflFixturePK.setRound(mapping.getAflRound());
					aflFixturePK.setGame(mapping.getAflGame());
					dflAflGames.add(aflFixtureService.get(aflFixturePK));
				}
			}

			loggerUtils.log("info", "Processing fixtures: DFL round={}; fixtures={}", roundInfo.getRound(), dflAflGames);
			processFixtures(roundInfo.getRound(), dflAflGames);
		}
	}

	@Override
	protected void closeServices() {
		if (dflRoundInfoService != null) {
			dflRoundInfoService.close();
		}
		if (aflFixtureService != null) {
			aflFixtureService.close();
		}
	}
	
	private void processFixtures(int dflRound, List<AflFixture> aflGames) throws Exception {
		
		Collections.sort(aflGames);
		
		int currentGameDay = 0;
		int previousGameDay = 0;
		
		Calendar startTimeCal = null;
		
		for(AflFixture game : aflGames) {
			
			loggerUtils.log("info", "AFL Fixture={}", game);
			
			startTimeCal = GregorianCalendar.from(game.getStartTime());
			currentGameDay = startTimeCal.get(Calendar.DAY_OF_WEEK);
			
			loggerUtils.log("info", "Current Game Day={}; Previous Game Day={};", currentGameDay, previousGameDay);
			
			if(currentGameDay != previousGameDay) {
				if(currentGameDay == Calendar.SUNDAY || currentGameDay == Calendar.SATURDAY) {
					loggerUtils.log("info", "Creating weekend run, start time={}", startTimeCal);
					createWeekendSchedule(dflRound, startTimeCal);
				} else {
					loggerUtils.log("info", "Creating weekday run, start time={}", startTimeCal);
					createWeekdaySchedule(dflRound, startTimeCal);
				}
				
				previousGameDay = currentGameDay;
			}
			
		}

		if (startTimeCal != null) {
			loggerUtils.log("info", "Creating final run, start time={}", startTimeCal);
			createFinalRunSchedule(dflRound, startTimeCal);
		} else {
			loggerUtils.log("info", "No AFL games found for DFL round={}, skipping final run schedule", dflRound);
		}
	}
	
	private void createWeekendSchedule(int dflRound, Calendar time) throws Exception {
		time.set(Calendar.HOUR_OF_DAY, 19);
		time.set(Calendar.MINUTE, 0);
		scheduleJob(dflRound, false, time);
			
		time.set(Calendar.HOUR_OF_DAY, 23);
		time.set(Calendar.MINUTE, 0);
		scheduleJob(dflRound, false, time);	
	}
	
	private void createWeekdaySchedule(int dflRound, Calendar time) throws Exception {
		time.set(Calendar.HOUR_OF_DAY, 23);
		time.set(Calendar.MINUTE, 0);
		scheduleJob(dflRound, false, time);	
	}
	
	private void createFinalRunSchedule(int dflRound, Calendar time) throws Exception {
		time.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH)+1);
		time.set(Calendar.HOUR_OF_DAY, 9);
		time.set(Calendar.MINUTE, 0);
		scheduleJob(dflRound, true, time);	
	}
	
	private void scheduleJob(int round, boolean isFinal, Calendar time) throws Exception {
		ZonedDateTime scheduledTime = ZonedDateTime.ofInstant(time.toInstant(), time.getTimeZone().toZoneId());

		Map<String, Object> jobParams = JobScheduleHelper.createJobParams(JobParameterConstants.PARAM_ROUND, round);
		jobParams.put(JobParameterConstants.PARAM_IS_FINAL, isFinal);

		JobScheduleHelper.scheduleJob(JobParameterConstants.RAW_STATS_REPORT_JOB_NAME, JobParameterConstants.RAW_STATS_REPORT_JOB_GROUP, JobParameterConstants.RAW_STATS_REPORT_JOB_CLASS, jobParams, scheduledTime);
	}
	
	public static void main(String[] args) {		
		RawStatsReportJobGenerator testing = new RawStatsReportJobGenerator();
		testing.execute();
	}
}
