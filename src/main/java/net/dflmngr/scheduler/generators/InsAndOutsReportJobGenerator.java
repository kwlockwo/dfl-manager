package net.dflmngr.scheduler.generators;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dflmngr.model.entity.DflRoundEarlyGames;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.scheduler.JobScheduler;
import net.dflmngr.utils.CronExpressionCreator;
import net.dflmngr.utils.DflmngrUtils;

public class InsAndOutsReportJobGenerator extends BaseJobGenerator {

	private static final String JOB_NAME = "InsOutsReport";
	private static final String JOB_GROUP = "InsOutsReports";
	private static final String JOB_CLASS = "net.dflmngr.scheduler.jobs.InsAndOutsReportJob";

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

	public InsAndOutsReportJobGenerator() {
		super("InsAndOutsReportJobGenerator");
		this.dflRoundInfoService = serviceFactory.createDflRoundInfoService();
		this.globalsService = serviceFactory.createGlobalsService();
	}

	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JOB_GROUP);

		List<DflRoundInfo> dflRounds = dflRoundInfoService.findAll();

		for(DflRoundInfo dflRound : dflRounds) {
			loggerUtils.log("info", "Creating full report job entry for round={}, lockout={}", dflRound.getRound(), dflRound.getHardLockoutTime());
			createReportJobEntryForFull(dflRound.getRound(), dflRound.getHardLockoutTime());

			Set<ZonedDateTime> earlyGameDates = new HashSet<>();
			for(DflRoundEarlyGames earlyGame : dflRound.getEarlyGames()) {
				loggerUtils.log("info", "Adding early games round={}, start time={}", dflRound.getRound(), earlyGame.getStartTime());
				earlyGameDates.add(earlyGame.getStartTime());
			}

			loggerUtils.log("info", "Creating partial report job entry for round={}", dflRound.getRound());
			createReportJobEntryForPartial(dflRound.getRound(), earlyGameDates);
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
	
	private void createReportJobEntryForFull(int round, ZonedDateTime time) throws Exception {
		
		CronExpressionCreator cronExpression = new CronExpressionCreator();
		Calendar timeCal = GregorianCalendar.from(time);
		
		int mins = timeCal.get(Calendar.MINUTE) + 10;
		if(mins >= 60) {
			timeCal.set(Calendar.MINUTE, (mins - 60));
			timeCal.set(Calendar.HOUR, (timeCal.get(Calendar.HOUR) + 1));
		} else {
			timeCal.set(Calendar.MINUTE, mins);
		}
		
		cronExpression.setTime(timeFormat.format(timeCal.getTime()));
		cronExpression.setStartDate(dateFormat.format(timeCal.getTime()));
        
		Map<String, Object> jobParams = new HashMap<>();
		jobParams.put("ROUND", round);
		jobParams.put("REPORT_TYPE","Full");

		JobScheduler.schedule(JOB_NAME, JOB_GROUP, JOB_CLASS, jobParams, cronExpression.getCronExpression(), false);
	}
	
	private void createReportJobEntryForPartial(int round, Set<ZonedDateTime> times) throws Exception {
		
		Set<String> runDates = new HashSet<>();
				
		for(ZonedDateTime time : times) {
			String timeStr = dateFormat.format(time);
			if(!runDates.contains(timeStr)) {
				runDates.add(timeStr);
			}
		}
		
		loggerUtils.log("info", "Partial report will run at the following times: {}", runDates);
		
		String standardLockout = globalsService.getStandardLockoutTime();
		int standardLockoutHour = Integer.parseInt((standardLockout.split(";"))[1]);
		int standardLockoutMinute = Integer.parseInt((standardLockout.split(";"))[2]);
		String standardLockoutHourAMPM = (standardLockout.split(";"))[3];
		
		for(String runDate : runDates) {
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(dateFormat.parse(runDate));
			
			timeCal.set(Calendar.HOUR, standardLockoutHour);
			timeCal.set(Calendar.MINUTE, standardLockoutMinute + 10);
			timeCal.set(Calendar.AM_PM, DflmngrUtils.AMPM.get(standardLockoutHourAMPM));
			
			CronExpressionCreator cronExpression = new CronExpressionCreator();
			cronExpression.setTime(timeFormat.format(timeCal.getTime()));
			cronExpression.setStartDate(dateFormat.format(timeCal.getTime()));
	        
			Map<String, Object> jobParams = new HashMap<>();
			jobParams.put("ROUND", round);
			jobParams.put("REPORT_TYPE","Partial");

			JobScheduler.schedule(JOB_NAME, JOB_GROUP, JOB_CLASS, jobParams, cronExpression.getCronExpression(), false);
		}
	}
	
	public static void main(String[] args) {
		InsAndOutsReportJobGenerator testing = new InsAndOutsReportJobGenerator();
		testing.execute();
	}
}
