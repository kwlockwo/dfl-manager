package net.dflmngr.reports;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import net.dflmngr.jndi.JndiProvider;
import net.dflmngr.logging.LoggingUtils;
import net.dflmngr.model.DomainDecodes;
import net.dflmngr.model.entity.DflFixture;
import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.model.entity.InsAndOuts;
import net.dflmngr.model.service.DflFixtureService;
import net.dflmngr.model.service.DflTeamPredictedScoresService;
import net.dflmngr.model.service.DflTeamService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.model.service.InsAndOutsService;
import net.dflmngr.model.service.impl.DflFixtureServiceImpl;
import net.dflmngr.model.service.impl.DflTeamPredictedScoresServiceImpl;
import net.dflmngr.model.service.impl.DflTeamServiceImpl;
import net.dflmngr.model.service.impl.GlobalsServiceImpl;
import net.dflmngr.model.service.impl.InsAndOutsServiceImpl;
//import net.dflmngr.utils.AmazonS3Utils;
import net.dflmngr.utils.DflmngrUtils;
import net.dflmngr.utils.EmailUtils;

public class InsAndOutsReport {
	private LoggingUtils loggerUtils;
	String defaultMdcKey = "online.name";
	String defaultLoggerName = "online-logger";
	String defaultLogfile = "InsAndOutsReport";
	
	String mdcKey;
	String loggerName;
	String logfile;
	
	GlobalsService globalsService;
	InsAndOutsService insAndOutsService;
	DflTeamService dflTeamService;
	DflFixtureService dflFixtureService;
	DflTeamPredictedScoresService dflTeamPredictedScoresService;
	
	String reportType;
	
	String emailOverride;
	boolean isExecutable;
		
	public InsAndOutsReport() {
		globalsService = new GlobalsServiceImpl();
		insAndOutsService = new InsAndOutsServiceImpl();
		dflTeamService = new DflTeamServiceImpl();
		dflFixtureService = new DflFixtureServiceImpl();
		dflTeamPredictedScoresService = new DflTeamPredictedScoresServiceImpl();
		isExecutable = false;
	}
	
	public void configureLogging(String mdcKey, String loggerName, String logfile) {
		this.mdcKey = mdcKey;
		this.loggerName = loggerName;
		this.logfile = logfile;
		
		//loggerUtils = new LoggingUtils(loggerName, mdcKey, logfile);
		loggerUtils = new LoggingUtils(logfile);
		isExecutable = true;
	}
	
	public void execute(int round, String reportType, String emailOverride) {
		
		try {
			if(!isExecutable) {
				configureLogging(defaultMdcKey, defaultLoggerName, defaultLogfile);
				loggerUtils.log("info", "Default logging configured");
			}
			
			loggerUtils.log("info", "Executing InsAndOutsReport for rounds: {}, report type: {}", round, reportType);
			
			if(emailOverride != null && !emailOverride.equals("")) {
				this.emailOverride = emailOverride;
			}
					
			//List<String> teams = globalsService.getTeamCodes();
			List<DflTeam> teams = dflTeamService.findAll();
			Map<String, List<Integer>> ins = new  HashMap<>();
			Map<String, List<Integer>> outs = new  HashMap<>();
			Map<String, Integer> emg1s = new HashMap<>();
			Map<String, Integer> emg2s = new HashMap<>();
			
			this.reportType = reportType;
			
			loggerUtils.log("info", "Team codes: {}", teams);
			
			for(DflTeam team : teams) {
				List<Integer> teamIns = new ArrayList<>();
				List<Integer> teamOuts = new ArrayList<>();
				int emg1 = 0;
				int emg2 = 0;
				
				
				List<InsAndOuts> teamInsAndOuts = insAndOutsService.getByTeamAndRound(round, team.getTeamCode());
				
				for(InsAndOuts inOrOut : teamInsAndOuts) {
					if(inOrOut.getInOrOut().equals(DomainDecodes.INS_AND_OUTS.IN_OR_OUT.IN)) {
						teamIns.add(inOrOut.getTeamPlayerId());
					} else if(inOrOut.getInOrOut().equals(DomainDecodes.INS_AND_OUTS.IN_OR_OUT.OUT)) {
						teamOuts.add(inOrOut.getTeamPlayerId());
					} else {
						if(inOrOut.getInOrOut().equals(DomainDecodes.INS_AND_OUTS.IN_OR_OUT.EMG1)) {
							emg1 = inOrOut.getTeamPlayerId();
						} else {
							emg2 = inOrOut.getTeamPlayerId();
						}
					}
				}
				
				loggerUtils.log("info", "{} ins: {}", team.getTeamCode(), teamIns);
				
				if(round == 1) {
					loggerUtils.log("info", "{} no outs round 1", team.getTeamCode());
				} else {
					loggerUtils.log("info", "{} outs: {}", team.getTeamCode(), teamOuts);
				}
				
				loggerUtils.log("info", "{} emg1: {}, emg2: {}", team.getTeamCode(), emg1, emg2);
				
				ins.put(team.getTeamCode(), teamIns);
				outs.put(team.getTeamCode(), teamOuts);
				emg1s.put(team.getTeamCode(), emg1);
				emg2s.put(team.getTeamCode(), emg2);
			}
	
			String report = writeReport(teams, round, ins, outs, emg1s, emg2s);
			
			if(reportType.equals("Full")) {
				loggerUtils.log("info", "Sending Full Report");
				emailReport(teams, report, round, true);
			} else {
				loggerUtils.log("info", "Sending Partial Report");
				emailReport(teams, report, round, false);
			}
			
			globalsService.close();
			insAndOutsService.close();
			dflTeamService.close();
			dflFixtureService.close();
			dflTeamPredictedScoresService.close();
			
		} catch (Exception ex) {
			loggerUtils.log("error", "Error in ... ", ex);
		}
	}
	
	private String writeReport(List<DflTeam> teams, int round, Map<String, List<Integer>> ins, Map<String, List<Integer>> outs, Map<String, Integer> emg1s, Map<String, Integer> emg2s) throws Exception {
		
		String reportName = "InsAndOutsReport_" + this.reportType + "_" + DflmngrUtils.getNowStr() + ".xlsx";
		
		Path reportDir = Paths.get(globalsService.getAppDir(), globalsService.getReportDir(), "insAndOutsReport");
		
		File directory = new File(reportDir.toString());
	    if (!directory.exists()){
	        directory.mkdirs();
	    }
		
		Path reportLocation = Paths.get(reportDir.toString(), reportName);
		
		loggerUtils.log("info", "Writing insAndOuts Report");
		loggerUtils.log("info", "Report name: {}", reportName);
		loggerUtils.log("info", "Report location: {}", reportLocation);
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Ins And Outs");
		
		setupReportRows(sheet, teams);
		addSelectionsToReport(sheet, teams, ins, "Ins");
		
		if(round > 1) {
			addSelectionsToReport(sheet, teams, outs, "Outs");
		}
		
		addEmgsToReport(sheet, teams, emg1s, emg2s);
		
		OutputStream out = Files.newOutputStream(reportLocation);
		workbook.write(out);
		workbook.close();
		out.close();
		
		//String s3key = Paths.get("insAndOutsReport", reportName).toString();
		
		//AmazonS3Utils.uploadToS3(s3key, reportLocation.toString());
		
		return reportLocation.toString();
	}
	
	private void setupReportRows(XSSFSheet sheet, List<DflTeam> teams) {
		
		loggerUtils.log("info", "Initlizing report rows");
		
		for(int i = 0; i <= 27; i++) {
			sheet.createRow(i);
		}

		XSSFRow row = sheet.getRow(0);
		
		int columnIndex = 1;
		for(DflTeam team : teams) {
			XSSFCell cell = row.createCell(columnIndex++);
			cell.setCellValue(team.getTeamCode());
		}
	}
	
	private void addSelectionsToReport(XSSFSheet sheet, List<DflTeam> teams, Map<String, List<Integer>> selections, String selectionType) {
		
		XSSFRow row;
		XSSFCell cell;
		
		loggerUtils.log("info", "Writing report data for: {}", selectionType);
		
		if(selectionType.equals("Ins")) {
			row = sheet.getRow(1);
			cell = row.createCell(0);
			cell.setCellValue("In");
		} else {
			row = sheet.getRow(13);
			cell = row.createCell(0);
			cell.setCellValue("Out");
		}
		
		for(DflTeam team : teams) {
			
			row = sheet.getRow(0);
			Iterator<Cell> cellIterator = row.cellIterator();
			
			int columnIndex = 1;
			while(cellIterator.hasNext()) {
				cell = (XSSFCell) cellIterator.next();
				
				if(cell.getStringCellValue().equals(team.getTeamCode())) {
					break;
				}
				
				columnIndex++;
			}
			
			List<Integer> teamSelections = selections.get(team.getTeamCode());
			
			int rowIndex = 1;
			if(selectionType.equals("Outs")) {
				rowIndex = 13;
			}
			
			for(Integer selection : teamSelections) {
					row = sheet.getRow(rowIndex++);
					if(row == null) {
						row = sheet.createRow(rowIndex-1);
					}
					cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellValue(selection);
			}		
		}
	}
	
	private void addEmgsToReport(XSSFSheet sheet, List<DflTeam> teams, Map<String, Integer> emg1s, Map<String, Integer> emg2s) {
		
		XSSFRow row;
		XSSFCell cell;
		
		loggerUtils.log("info", "Writing report data for: Emgs");
		
		row = sheet.getRow(26);
		cell = row.createCell(0);
		cell.setCellValue("Emgs");
				
		for(DflTeam team : teams) {
			
			row = sheet.getRow(0);
			Iterator<Cell> cellIterator = row.cellIterator();
			
			int columnIndex = 1;
			while(cellIterator.hasNext()) {
				cell = (XSSFCell) cellIterator.next();
				
				if(cell.getStringCellValue().equals(team.getTeamCode())) {
					break;
				}
				
				columnIndex++;
			}
			
			int emg1 = emg1s.get(team.getTeamCode());
			int emg2 = emg2s.get(team.getTeamCode());
			
			if(emg1 > 0) {
				row = sheet.getRow(26);
				if(row == null) {
					row = sheet.createRow(26);
				}
				
				cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(emg1);
			}
			
			if(emg2 > 0) {
				row = sheet.getRow(27);
				if(row == null) {
					row = sheet.createRow(27);
				}
				
				cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(emg2);
			}
		}
	}
	
	private void emailReport(List<DflTeam> teams, String reportName, int round, boolean isFinal) throws Exception {
		
		String dflMngrEmail = globalsService.getEmailConfig().get("dflmngrEmailAddr");
		
		String subject = "";
		String body = "<html>\n<body>\n";
		
		if(isFinal) {
			subject = "Ins and Outs for DFL round " + round + " - FULL";
			body = "<p>Please find attached the full ins and outs for round " + round + ".</p>\n";
		} else {
			subject = "Ins and Outs for DFL round " + round + " - PARTIAL";
			body = "<p>Please find attached the partial ins and outs for round " + round + ". Team updates can still be made, further updates will be sent.</p>\n";
		}
		
		body = body + "<p>DFL Manager has made the following predictions:</p>\n";
		body = body + addPredictions(round);
		body = body + "<p>DFL Manager Admin</p>\n";
		body = body + "</body>\n</html>";
		
		List<String> to = new ArrayList<>();

		if(emailOverride != null && !emailOverride.equals("")) {
			to.add(emailOverride);
		} else {
			for(DflTeam team : teams) {
				to.add(team.getCoachEmail());
			}
		}
		
		List<String> attachments = new ArrayList<>();
		attachments.add(reportName);
		
		loggerUtils.log("info", "Emailing to={}; reportName={}", to, reportName);
		EmailUtils.sendHtmlEmail(to, dflMngrEmail, subject, body, attachments);
	}
	
	private String addPredictions(int round) {
		
		String predictionsStr = "";
		predictionsStr = predictionsStr + "<p><ul type=none>\n";
		
		List<DflFixture> roundFixtures = dflFixtureService.getFixturesForRound(round);
		
		for(DflFixture fixture : roundFixtures) {
			DflTeam homeTeam = dflTeamService.get(fixture.getHomeTeam());
			int homeTeamPredictedScore = dflTeamPredictedScoresService.getTeamPredictedScoreForRound(homeTeam.getTeamCode(), round).getPredictedScore();
			
			DflTeam awayTeam = dflTeamService.get(fixture.getAwayTeam());
			int awayTeamPredictedScore = dflTeamPredictedScoresService.getTeamPredictedScoreForRound(awayTeam.getTeamCode(), round).getPredictedScore();
			
			String resultString = "";
			if(homeTeamPredictedScore > awayTeamPredictedScore) {
				resultString = " to defeat ";
			} else {
				resultString = " to be defeated by ";
			}
			
			String gameUrl = globalsService.getOnlineBaseUrl() + "/results/" + fixture.getRound() + "/" + fixture.getGame();
			
			predictionsStr = predictionsStr + "<li>" + homeTeam.getName() + " " + resultString + awayTeam.getName() + ", " + homeTeamPredictedScore + " to " + awayTeamPredictedScore + 
					   " - <a href=\"" + gameUrl + "\">Match Report</a></li>\n";

		}
		
		predictionsStr = predictionsStr + "</ul></p>\n";
		
		return predictionsStr;
	}
	
	// For internal testing
	public static void main(String[] args) {
		
		try {
			String email = "";
			int round = 0;
			String reportType = "";
			
			if(args.length > 3 || args.length < 2) {
				System.out.println("usage: RawStatsReport <round> Full|Partial optional [<email>]");
			} else {
				
				round = Integer.parseInt(args[0]);
				
				if(args.length == 2) {
					reportType = args[1];
				} else if(args.length == 3) {
					reportType = args[1];
					email = args[2];
				}
				
				//JndiProvider.bind();
				
				InsAndOutsReport insAndOutsReport = new InsAndOutsReport();
				insAndOutsReport.configureLogging("batch.name", "batch-logger", "InsAndOutsReport");
				insAndOutsReport.execute(round, reportType, email);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
