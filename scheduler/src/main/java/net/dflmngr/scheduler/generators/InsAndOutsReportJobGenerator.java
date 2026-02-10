package net.dflmngr.scheduler.generators;

import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dflmngr.model.entity.DflRoundEarlyGames;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.scheduler.JobParameterConstants;
import net.dflmngr.scheduler.JobScheduleHelper;
import net.dflmngr.scheduler.JobScheduler;
import net.dflmngr.utils.DflmngrUtils;

@Component
public class InsAndOutsReportJobGenerator extends BaseJobGenerator {

	public InsAndOutsReportJobGenerator() {
		super("InsAndOutsReportJobGenerator");
	}

	private DflRoundInfoService dflRoundInfoService;
	private GlobalsService globalsService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


	@Override
	protected void generateJobs() throws Exception {
		JobScheduler.deleteGroup(JobParameterConstants.INS_OUTS_REPORT_JOB_GROUP);

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

		ZonedDateTime scheduledTime = time.plusMinutes(10);

		Map<String, Object> jobParams = JobScheduleHelper.createJobParams(JobParameterConstants.PARAM_ROUND, round);
		jobParams.put(JobParameterConstants.PARAM_REPORT_TYPE,"Full");

		JobScheduleHelper.scheduleJob(JobParameterConstants.INS_OUTS_REPORT_JOB_NAME, JobParameterConstants.INS_OUTS_REPORT_JOB_GROUP, JobParameterConstants.INS_OUTS_REPORT_JOB_CLASS, jobParams, scheduledTime);
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

			ZonedDateTime scheduledTime = ZonedDateTime.ofInstant(timeCal.toInstant(), timeCal.getTimeZone().toZoneId());

			Map<String, Object> jobParams = JobScheduleHelper.createJobParams(JobParameterConstants.PARAM_ROUND, round);
			jobParams.put(JobParameterConstants.PARAM_REPORT_TYPE,"Partial");

			JobScheduleHelper.scheduleJob(JobParameterConstants.INS_OUTS_REPORT_JOB_NAME, JobParameterConstants.INS_OUTS_REPORT_JOB_GROUP, JobParameterConstants.INS_OUTS_REPORT_JOB_CLASS, jobParams, scheduledTime);
		}
	}
	
	public static void main(String[] args) {
		InsAndOutsReportJobGenerator testing = new InsAndOutsReportJobGenerator();
		testing.execute();
	}
}
