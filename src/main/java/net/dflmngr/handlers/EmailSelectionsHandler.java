package net.dflmngr.handlers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.Part;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import com.sun.mail.imap.IMAPFolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.entity.DflTeam;
import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.model.service.DflTeamPlayerService;
import net.dflmngr.model.service.DflTeamService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.utils.DflmngrUtils;
import net.dflmngr.utils.oauth2.OAuth2Authenticator;
import net.dflmngr.validation.SelectedTeamValidation;
import net.freeutils.tnef.Attachment;
import net.freeutils.tnef.TNEFInputStream;

public class EmailSelectionsHandler extends BaseHandler {

	private static final String TAG_TEAM = "[team]";
	private static final String TAG_END = "[end]";
	private static final String TAG_START_ID = "[start id=";
	private static final String TAG_ROUND = "[round]";
	private static final String TAG_IN = "[in]";
	private static final String TAG_OUT = "[out]";
	private static final String TAG_EMG = "[emg]";

	private String dflmngrEmailAddr;
	private String incomingMailHost;
	private int incomingMailPort;
	private String outgoingMailHost;
	private int outgoingMailPort;
	private String mailUsername;
	private String mailPassword;

	private String emailOveride;

	GlobalsService globalsService;
	DflTeamService dflTeamService;
	DflTeamPlayerService dflTeamPlayerService;

	List<SelectedTeamValidation> validationResults;
	Map<String, String> selectionsIdsCurrent;

	public EmailSelectionsHandler() {
		super("Selections");
		globalsService = serviceFactory.createGlobalsService();
		dflTeamService = serviceFactory.createDflTeamService();
		dflTeamPlayerService = serviceFactory.createDflTeamPlayerService();
	}

	public void execute() {
		try {
			ensureLoggingConfigured();

			validationResults = new ArrayList<>();
			selectionsIdsCurrent = new HashMap<>();

			loggerUtils.log("info", "Email Selections Handler is executing ....");

			Map<String, String> emailConfig = globalsService.getEmailConfig();

			this.dflmngrEmailAddr = emailConfig.get("dflmngrEmailAddr");
			this.incomingMailHost = emailConfig.get("incomingMailHost");
			this.incomingMailPort = Integer.parseInt(emailConfig.get("incomingMailPort"));
			this.outgoingMailHost = emailConfig.get("outgoingMailHost");
			this.outgoingMailPort = Integer.parseInt(emailConfig.get("outgoingMailPort"));
			this.mailUsername = emailConfig.get("mailUsername");
			this.mailPassword = emailConfig.get("mailPassword");

			this.emailOveride = "";

			if (!"production".equals(System.getenv("ENV"))) {
				this.dflmngrEmailAddr = System.getenv("DFL_MNGR_EMAIL");
				this.mailUsername = System.getenv("DFL_MNGR_EMAIL");
				this.emailOveride = System.getenv("EMAIL_OVERIDE");
			}

			loggerUtils.log("info",
					"Email config: dflmngrEmailAddr={}; incomingMailHost={}; incomingMailPort={}; outgoingMailHost={}; outgoingMailHost={}; mailUsername={}; mailPassword={}",
					dflmngrEmailAddr, incomingMailHost, incomingMailPort, outgoingMailHost, outgoingMailPort,
					mailUsername, "*".repeat(mailPassword != null ? mailPassword.length() : 0));

			OAuth2Authenticator.initialize();

			processSelections();

			loggerUtils.log("info", "Sending responses");

			sendResponses();

			loggerUtils.log("info", "Email Selections Handler Completed");
		} catch (Exception ex) {
			loggerUtils.logException("Error in EmailSelectionsHandler.execute()", ex);
		} finally {
			globalsService.close();
			dflTeamService.close();
			dflTeamPlayerService.close();
		}
	}

	private void processSelections() throws Exception {

		Properties properties = new Properties();
		properties.setProperty("mail.imap.ssl.enable", "true");

		Session session = Session.getInstance(properties);
		Store store = session.getStore("imap");
		store.connect(incomingMailHost, incomingMailPort, mailUsername, mailPassword);

		Folder inbox = store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);

		Message[] messages = inbox.getMessages();

		loggerUtils.log("info", "Opended inbox: messages={}", messages.length);

		for (int i = 0; i < messages.length; i++) {

			loggerUtils.log("debug", "Handling message {}", i);

			SelectedTeamValidation validationResult = null;

			try {

				Message message = messages[i];
				String from = InternetAddress.toString(message.getFrom());
				Instant instant = message.getReceivedDate() != null ? message.getReceivedDate().toInstant() : Instant.now();
				ZonedDateTime receivedDate = ZonedDateTime.ofInstant(instant, ZoneId.of(DflmngrUtils.defaultTimezone));

				if(message.isMimeType("multipart/*")) {
					Object content = message.getContent();
					Multipart multipart = content instanceof Multipart
							? (Multipart) content
							: new MimeMultipart(message.getDataHandler().getDataSource());

					for (int j = 0; j < multipart.getCount(); j++) {
						BodyPart part = multipart.getBodyPart(j);
						validationResult = scanEmailPartsAndValidate(part, receivedDate, from);
						if (validationResult != null) {
							break;
						}
					}
				} else if(message.isMimeType("text/plain")) {
					validationResult = handleTextEmailContent(message.getContent().toString(), from);
				} else if(message.isMimeType("text/html")) {
					validationResult = handleHtmlEmailContent(message.getContent().toString(), from);
				}

				if (validationResult == null) {
					validationResult = new SelectedTeamValidation();
					loggerUtils.log("info", "Selection file or selection body is missing.");
					validationResult.selectionFileMissing = true;
					validationResult.setFrom(from);
					validationResults.add(validationResult);
					loggerUtils.log("info", "Message from {} ... FAILURE!", from);
				} else {
					if (validationResult.isValid()) {
						TeamInsOutsLoaderHandler selectionsLoader = new TeamInsOutsLoaderHandler();
						selectionsLoader.configureLogging(mdcKey, loggerName, logfile);

						if (validationResult.earlyGames) {
							loggerUtils.log("info",
									"Early Games any validation error is a warning .... Saving ins and outs to early tables in DB");
							selectionsLoader.execute(validationResult.getTeamCode(), validationResult.getRound(),
									validationResult.getInsAndOuts().get("in"),
									validationResult.getInsAndOuts().get("out"), validationResult.getEmergencies(),
									true);
						} else {
							loggerUtils.log("info", "Team selection is VALID.... Saving ins and outs to DB");
							selectionsLoader.execute(validationResult.getTeamCode(), validationResult.getRound(),
									validationResult.getInsAndOuts().get("in"),
									validationResult.getInsAndOuts().get("out"), validationResult.getEmergencies(),
									false);
						}
					} else {
						loggerUtils.log("info", "Team selection is invalid ... No changes made.");
					}
				}
			} catch (Exception ex) {
				loggerUtils.logException("Error in EmailSelectionsHandler processing message " + i + " from inbox", ex);
				try {
					String from = InternetAddress.toString(messages[i].getFrom());
					validationResult = new SelectedTeamValidation();
					validationResult.unknownError = true;
					validationResult.selectionFileMissing = false;
					validationResult.roundCompleted = false;
					validationResult.lockedOut = false;
					validationResult.setFrom(from);
					validationResults.add(validationResult);
					loggerUtils.log("info", "Message from {} ... FAILURE with EXCEPTION!", from);
				} catch (MessagingException ex2) {
					loggerUtils.log("error", "Error retrieving sender address for failed message", ex2);
				}
			}
		}

		loggerUtils.log("info", "Moving messages to Processed folder");
		Folder processedMessages = store.getFolder("Processed");
		((IMAPFolder)inbox).moveMessages(messages, processedMessages);

		inbox.close(true);
		store.close();
	}

	private SelectedTeamValidation scanEmailPartsAndValidate(BodyPart part, ZonedDateTime receivedDate, String from)
			throws Exception {

		SelectedTeamValidation validationResult = null;

		Object content = part.getContent();

if (content instanceof InputStream || content instanceof String) {

			if (part.isMimeType("text/plain")) {
				String text;
				if (content instanceof InputStream) {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
						text = reader.lines().collect(java.util.stream.Collectors.joining("\n"));
					}
				} else {
					text = content.toString();
				}
				validationResult = handleTextEmailContent(text.trim(), from);
			} else if (part.isMimeType("text/html")) {
				String text;
				if (content instanceof InputStream) {
					try (BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8))) {
						text = reader.lines().collect(java.util.stream.Collectors.joining("\n"));
					}
				} else {
					text = content.toString();
				}
				validationResult = handleHtmlEmailContent(text, from);
			}

			if (validationResult == null) {
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
						|| Part.INLINE.equalsIgnoreCase(part.getDisposition())
						|| (part.getFileName() != null && !part.getFileName().isEmpty())) {
					String attachementName = part.getFileName();
					loggerUtils.log("info", "Attachement found, name={}", attachementName);
					if (attachementName == null) {
						loggerUtils.log("info", "Attachement has no name, skipping");
					} else if (attachementName.equalsIgnoreCase("selections.txt")) {
						loggerUtils.log("info", "Message from {}, has selection attachment", from);
							validationResult = handleSelectionFile(part.getInputStream());
						validationResult.setFrom(from);
						validationResults.add(validationResult);
						loggerUtils.log("info", "Message from {} handled with ... SUCCESS!", from);
					} else if (attachementName.equalsIgnoreCase("WINMAIL.DAT")
							|| attachementName.equalsIgnoreCase("ATT00001.DAT")) {
						loggerUtils.log("info", "Message from {}, is a TNEF message", from);
						validationResult = handleTNEFMessage(part.getInputStream(), from);
						if (validationResult != null) {
							validationResult.setFrom(from);
							validationResults.add(validationResult);
							loggerUtils.log("info", "Message from {} handled with ... SUCCESS!", from);
						} else {
							loggerUtils.log("info", "Message from {}, TNEF message has no selections.txt", from);
						}
					}
				}
			}
		}

		if (validationResult == null) {
			Multipart multipart = null;
			if (content instanceof Multipart) {
				multipart = (Multipart) content;
			} else if (content instanceof InputStream && part.isMimeType("multipart/*")) {
				multipart = new MimeMultipart(part.getDataHandler().getDataSource());
			}
			if (multipart != null) {
				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					validationResult = scanEmailPartsAndValidate(bodyPart, receivedDate, from);
					if (validationResult != null) {
						break;
					}
				}
			}
		}

		return validationResult;
	}

	private SelectedTeamValidation handleTextEmailContent(String text, String from) {
		SelectedTeamValidation validationResult = null;

		text = normaliseTagsToLines(text);

		if (text.startsWith(TAG_TEAM) && text.contains(TAG_END)) {
			text = text.substring(0, text.indexOf(TAG_END));
			String[] lines = text.split("\\R+");

			loggerUtils.log("info", "Message from {}, has selection in text body", from);
			validationResult = handleSelectionEmailText(lines, "noid");
			validationResult.setFrom(from);
			validationResults.add(validationResult);
		} else if (text.contains(TAG_START_ID) && text.contains(TAG_END)) {
			text = text.substring(text.indexOf(TAG_START_ID), text.indexOf(TAG_END));
			String[] lines = text.split("\\R+");

			String idLine = lines[0];
			String id = idLine.split("=")[1].trim().replace("]", "");

			loggerUtils.log("info", "Message from {}, has selection in text body", from);
			validationResult = handleSelectionEmailText(lines, id);
			validationResult.setFrom(from);
			validationResults.add(validationResult);
		}

		return validationResult;
	}

	private String normaliseTagsToLines(String text) {
		String[] tags = { TAG_TEAM, TAG_ROUND, TAG_IN, TAG_OUT, TAG_EMG, TAG_END, TAG_START_ID };
		String[] tagsNeedingTrailingNewline = { TAG_TEAM, TAG_ROUND, TAG_IN, TAG_OUT, TAG_EMG, TAG_END };
		for (String tag : tags) {
			text = text.replaceAll("(?i)(?<!\n)(" + Pattern.quote(tag) + ")", "\n$1");
		}
		for (String tag : tagsNeedingTrailingNewline) {
			text = text.replaceAll("(?i)(" + Pattern.quote(tag) + ")(?!\n)", "$1\n");
		}
		// Split concatenated players e.g. "21 HARDWICK41 PINK" -> "21 HARDWICK\n41 PINK"
		// Also handles non-breaking space separator e.g. "17 Lipinsky Fwd\u00A07 Caldwell"
		text = text.replaceAll("(?<=[A-Za-z])[\\s\u00A0]*(?=\\d)", "\n");
		return text.trim();
	}

	private SelectedTeamValidation handleHtmlEmailContent(String content, String from) {
		SelectedTeamValidation validationResult = null;

		Document document = Jsoup.parse(content);
		document.outputSettings(new Document.OutputSettings().prettyPrint(false));// makes html() preserve
																					// linebreaks and spacing
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		String s = document.body().html().replaceAll("\\\\n", "\n");
		String text = Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false))
				.trim();

		if (text.startsWith(TAG_TEAM) && text.contains(TAG_END)) {
			text = text.substring(0, text.indexOf(TAG_END));
			String[] lines = text.split("\\R+");

			loggerUtils.log("info", "Message from {}, has selection in html body", from);
			validationResult = handleSelectionEmailText(lines, "noid");
			validationResult.setFrom(from);
			validationResults.add(validationResult);
		} else if (text.contains(TAG_START_ID) && text.contains(TAG_END)) {
			text = text.substring(text.indexOf(TAG_START_ID), text.indexOf(TAG_END));
			String[] lines = text.split("\\R+");

			String idLine = lines[0];
			String id = idLine.split("=")[1].trim().replace("]", "");

			loggerUtils.log("info", "Message from {}, has selection in text body", from);
			validationResult = handleSelectionEmailText(lines, id);
			validationResult.setFrom(from);
			validationResults.add(validationResult);
		}

		return validationResult;
	}

	private SelectedTeamValidation handleTNEFMessage(InputStream inputStream, String from)
			throws Exception {

		SelectedTeamValidation validationResult = null;

		TNEFInputStream tnefInputSteam = new TNEFInputStream(inputStream);
		net.freeutils.tnef.Message message = new net.freeutils.tnef.Message(tnefInputSteam);

		for (Attachment attachment : message.getAttachments()) {
			if (attachment.getNestedMessage() == null) {
				String filename = attachment.getFilename();

				if ("selections.txt".equals(filename)) {
					loggerUtils.log("info", "Message from {}, has selection attachment", from);
					validationResult = handleSelectionFile(attachment.getRawData());
				}
			}
		}

		message.close();

		return validationResult;
	}

	private SelectedTeamValidation handleSelectionFile(InputStream inputStream)
			throws Exception {

		String line = "";
		String teamCode = "";
		int round = 0;
		List<Integer> ins = new ArrayList<>();
		List<Integer> outs = new ArrayList<>();
		List<Double> emgs = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

		loggerUtils.log("info", "Handling selections form file attachement");

		while ((line = reader.readLine()) != null) {

			if (line.toLowerCase().contains(TAG_TEAM)) {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.toLowerCase().contains(TAG_ROUND) || line.toLowerCase().contains(TAG_END)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						teamCode = line;
					}
				}
				loggerUtils.log("debug", "Selections for team: {}", teamCode);
				if (line == null) {
					break;
				}
			}

			if (line.toLowerCase().contains(TAG_ROUND)) {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.toLowerCase().contains(TAG_IN) || line.toLowerCase().contains(TAG_OUT)
							|| line.toLowerCase().contains(TAG_EMG) || line.toLowerCase().contains(TAG_END)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						round = Integer.parseInt(line);
					}
				}
				loggerUtils.log("debug", "Selections for round: {}", round);
				if (line == null) {
					break;
				}
			}

			if (line.toLowerCase().contains(TAG_IN)) {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.toLowerCase().contains(TAG_OUT)) {
						break;
					} else if (line.toLowerCase().contains(TAG_EMG)) {
						break;
					} else if (line.toLowerCase().contains(TAG_END)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						ins.add(Integer.parseInt(line));
					}
				}
				loggerUtils.log("debug", "Selection in: {}", ins);
				if (line == null) {
					break;
				}
			}

			if (line.toLowerCase().contains(TAG_OUT)) {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.toLowerCase().contains(TAG_IN)) {
						break;
					} else if (line.toLowerCase().contains(TAG_EMG)) {
						break;
					} else if (line.toLowerCase().contains(TAG_END)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						outs.add(Integer.parseInt(line));
					}
				}
				loggerUtils.log("debug", "Selection out: {}", outs);
				if (line == null) {
					break;
				}
			}

			if (line.toLowerCase().contains(TAG_EMG)) {
				int emgCount = 1;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.toLowerCase().contains(TAG_IN)) {
						break;
					} else if (line.toLowerCase().contains(TAG_OUT)) {
						break;
					} else if (line.toLowerCase().contains(TAG_END)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						double emg = Double.parseDouble(line);
						if (emgCount == 1) {
							emg = emg + 0.1;
							emgCount++;
						} else {
							emg = emg + 0.2;
						}
						emgs.add(emg);
					}
				}
				loggerUtils.log("debug", "Selection emergencies: {}", emgs);
				if (line == null) {
					break;
				}
			}
		}
		}

		Map<String, List<Integer>> insAndOuts = new HashMap<>();
		insAndOuts.put("in", ins);
		insAndOuts.put("out", outs);

		SelectedTeamValidationHandler validationHandler = new SelectedTeamValidationHandler();
		validationHandler.configureLogging(mdcKey, loggerName, logfile);
		return validationHandler.execute(round, teamCode, insAndOuts, emgs, "noid");
	}

	private SelectedTeamValidation handleSelectionEmailText(String[] emailLines, String id) {

		String line = "";
		String teamCode = "";
		int round = 0;
		List<Integer> ins = new ArrayList<>();
		List<Integer> outs = new ArrayList<>();
		List<Double> emgs = new ArrayList<>();

		loggerUtils.log("info", "Handling selections from email text.");

		for (int i = 0; i < emailLines.length; i++) {

			line = emailLines[i];

			if (line.toLowerCase().contains(TAG_TEAM)) {
				while (i < emailLines.length) {
					line = emailLines[i++].trim();
					if (line.toLowerCase().contains(TAG_ROUND)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						teamCode = line;
					}
				}
				loggerUtils.log("debug", "Selections for team: {}", teamCode);
			}

			if (line.toLowerCase().contains(TAG_ROUND)) {
				while (i < emailLines.length) {
					line = emailLines[i++].trim();
					if (line.toLowerCase().contains(TAG_IN) || line.toLowerCase().contains(TAG_OUT)
							|| line.toLowerCase().contains(TAG_EMG)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						round = Integer.parseInt(line);
					}
				}
				loggerUtils.log("debug", "Selections for round: {}", round);
			}

			if (line.toLowerCase().contains(TAG_IN)) {
				while (i < emailLines.length) {
					line = emailLines[i++].trim();
					if (line.toLowerCase().contains(TAG_OUT)) {
						break;
					} else if (line.toLowerCase().contains(TAG_EMG)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						int in = getPlayerNo(line);
						if (in > 0) {
							ins.add(in);
						} else {
							loggerUtils.log("debug", "Couldn't get player number for INs, No.={}", in);
						}
					}
				}
				loggerUtils.log("debug", "Selection in: {}", ins);
			}

			if (line.toLowerCase().contains(TAG_OUT)) {
				while (i < emailLines.length) {
					line = emailLines[i++].trim();
					if (line.toLowerCase().contains(TAG_IN)) {
						break;
					} else if (line.toLowerCase().contains(TAG_EMG)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						int out = getPlayerNo(line);
						if (out > 0) {
							outs.add(out);
						} else {
							loggerUtils.log("debug", "Couldn't get player number for OUTs, No.={}", out);
						}
					}
				}
				loggerUtils.log("debug", "Selection out: {}", outs);
			}

			if (line.toLowerCase().contains(TAG_EMG)) {
				int emgCount = 1;
				while (i < emailLines.length) {
					line = emailLines[i++].trim();
					if (line.toLowerCase().contains(TAG_IN)) {
						break;
					} else if (line.toLowerCase().contains(TAG_OUT)) {
						break;
					} else if (line.isEmpty()) {
						// ignore blank lines
					} else {
						double emg = getPlayerNo(line);
						if (emg > 0) {
							if (emgCount == 1) {
								emg = emg + 0.1;
								emgCount++;
							} else {
								emg = emg + 0.2;
							}
							emgs.add(emg);
						} else {
							loggerUtils.log("debug", "Couldn't get player number for OUTs, No.={}", emg);
						}
					}
				}
				loggerUtils.log("debug", "Selection emergencies: {}", emgs);
			}
		}

		boolean idHandledThisBatch = false;

		if (selectionsIdsCurrent.containsKey(id)) {
			String team = selectionsIdsCurrent.get(id);
			if (team.equalsIgnoreCase(teamCode)) {
				idHandledThisBatch = true;
			} else {
				selectionsIdsCurrent.put(id, teamCode);
			}
		} else {
			selectionsIdsCurrent.put(id, teamCode);
		}

		SelectedTeamValidation validationResult = null;

		if (idHandledThisBatch) {
			validationResult = new SelectedTeamValidation();
			validationResult.selectionFileMissing = false;
			validationResult.roundCompleted = false;
			validationResult.lockedOut = false;
			validationResult.duplicateSubmissionId = true;
			loggerUtils.log("info", "Already handled a selection in this batch for round={}. teamCode={}, id={}", round,
					teamCode, id);
		} else {
			Map<String, List<Integer>> insAndOuts = new HashMap<>();
			insAndOuts.put("in", ins);
			insAndOuts.put("out", outs);

			SelectedTeamValidationHandler validationHandler = new SelectedTeamValidationHandler();
			validationHandler.configureLogging(mdcKey, loggerName, logfile);
			validationResult = validationHandler.execute(round, teamCode, insAndOuts, emgs, id);
		}

		return validationResult;
	}

	private int getPlayerNo(String line) {

		int playerNo;
		String playerNoStr;

		line = line.replaceAll("^[|\\s\u00A0]+", "").trim();

		if (line.isEmpty()) {
			return 0;
		}

		Pattern pattern = Pattern.compile("[\\s\u00A0:\\-\\.\\W]");
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			int i = matcher.start();
			playerNoStr = line.substring(0, i);
		} else {
			playerNoStr = line;
		}

		try {
			playerNo = Integer.parseInt(playerNoStr.trim());
		} catch (NumberFormatException e) {
			loggerUtils.log("info", "Error parsing player number, number format exception ... oh well.  Error={}",
					e.getMessage());
			playerNo = 0;
		}

		return playerNo;
	}

	private void sendResponses() throws MessagingException {
		for (SelectedTeamValidation validationResult : validationResults) {

			String to = "";

			if (!"production".equals(System.getenv("ENV"))) {
				to = this.emailOveride;
			} else {
				to = validationResult.getFrom();
			}

			if (to == null || to.isEmpty()) {
				loggerUtils.log("warn", "No recipient for response (emailOveride/from not set), skipping response");
				continue;
			}

			String teamCode = validationResult.getTeamCode();

			Properties properties = new Properties();
			// Setup mail server
			properties.setProperty("mail.smtp.host", outgoingMailHost);
			properties.setProperty("mail.smtp.port", String.valueOf(outgoingMailPort));
			properties.setProperty("mail.smtp.starttls.enable", "true");
			properties.setProperty("mail.smtp.auth", "true");

			Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailUsername, mailPassword);
				}
			});

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.dflmngrEmailAddr));
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			loggerUtils.log("info", "Creating response message: to={}; from={};", to, this.dflmngrEmailAddr);

			if (validationResult.isValid()) {
				if (teamCode != null && !teamCode.equals("")) {
					DflTeam team = dflTeamService.get(teamCode);
					if (team != null) {
						String teamTo = team.getCoachEmail();
						loggerUtils.log("info", "Team email: {}", teamTo);
						if (teamTo != null && !to.toLowerCase().contains(teamTo.toLowerCase())) {
							loggerUtils.log("info", "Adding team email");
							message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(teamTo));
						}
					} else {
						loggerUtils.log("warn", "No team found for teamCode: {}", teamCode);
					}
				}
				loggerUtils.log("info", "Message is for SUCCESS");
				setSuccessMessage(message, validationResult);
			} else {
				loggerUtils.log("info", "Message is for FAILURE");
				setFailureMessage(message, validationResult);
			}

			loggerUtils.log("info", "Sending message");

			Transport.send(message, message.getAllRecipients());
		}
	}

	private String formatPlayerLine(DflPlayer player) {
		DflTeamPlayer teamPlayer = dflTeamPlayerService.get(player.getPlayerId());
		return "\t\t" + teamPlayer.getTeamPlayerId() + " " + player.getFirstName()
				+ " " + player.getLastName() + " " + player.getPosition() + " " + player.getAflClub() + "\n";
	}

	private void appendWarning(StringBuilder body, boolean condition, String message, List<DflPlayer> players) {
		if (condition) {
			body.append(message);
			for (DflPlayer player : players) {
				body.append(formatPlayerLine(player));
			}
		}
	}

	private void setSuccessMessage(Message message, SelectedTeamValidation validationResult) throws MessagingException {
		message.setSubject("Selections received - SUCCESS!");

		StringBuilder messageBody = new StringBuilder("Coach, \n\nYour selections have been stored in the database ....\n");

		if (validationResult.areWarnings()) {
			messageBody.append("\n");
			appendWarning(messageBody, validationResult.selectedWarning,
					"\tWarning: You have seleted a player who is already selected.  You may be playing short! Players:\n",
					validationResult.selectedWarnPlayers);
			appendWarning(messageBody, validationResult.droppedWarning,
					"\tWarning: You have dropped a player who is not selected.  Your team may not be as you expect or invalid! Players:\n",
					validationResult.droppedWarnPlayers);
			if(validationResult.emergencyWarning) {
				messageBody.append("\tWarning: Your emergency is invalid as the position is full on your bench.  It will be ignored.\n");
			}
			appendWarning(messageBody, validationResult.duplicateIns,
					"\tWarning: You have selected duplicate ins, one will be ignored.  Ins:\n",
					validationResult.dupInPlayers);
			appendWarning(messageBody, validationResult.duplicateOuts,
					"\tWarning: You have selected duplicate outs, one will be ignored.  Outs:\n",
					validationResult.dupOutPlayers);
			appendWarning(messageBody, validationResult.duplicateEmgs,
					"\tWarning: You have selected duplicate emergencies, one will be ignored.  Emergencies:\n",
					validationResult.dupEmgPlayers);
		}

		messageBody.append("\n\nHave a nice day. \n\nDFL Manager Admin");

		message.setContent(messageBody.toString(), "text/plain");
	}

	private void setFailureMessage(Message message, SelectedTeamValidation validationResult) throws MessagingException {
		message.setSubject("Selections received - FAILED!");

		StringBuilder messageBody = new StringBuilder("Coach,\n\nYour selections have not been stored in the database .... The reasons for this are:\n");

		if (validationResult.playedSelections) {
			messageBody.append("\t- You have selected/dropped a player who has already played and was not included in your previous selections.\n");
		} else if (validationResult.selectionFileMissing) {
			messageBody.append("\t- You sent the email with no selections.txt or the selections were missing from the emil body\n");
		} else if (validationResult.emptyTeam) {
			messageBody.append("\t- Your selections resulted in an empty team after applying ins and outs.\n");
		} else if (validationResult.roundCompleted) {
			messageBody.append("\t- The round you have in your selections.txt has past\n");
		} else if (validationResult.lockedOut) {
			messageBody.append("\t- The round you have in your selections as had all AFL games completed.\n");
		} else if (validationResult.unknownError) {
			messageBody.append("\t- Some exception occured follow up with email to xdfl google group.\n");
		} else if (validationResult.duplicateSubmissionId) {
			messageBody.append("\t- Already processed selection for your selection id.\n");
		} else if (!validationResult.teamPlayerCheckOk) {
			messageBody.append("\t- The ins and/or outs numbers sent are not correct\n");
		} else {
			if (!validationResult.ffCheckOk) {
				messageBody.append("\t- You have too many Full Forwards\n");
			}
			if (!validationResult.fwdCheckOk) {
				messageBody.append("\t- You have too many Forwards\n");
			}
			if (!validationResult.rckCheckOk) {
				messageBody.append("\t- You have too many Rucks\n");
			}
			if (!validationResult.midCheckOk) {
				messageBody.append("\t- You have too many Midfielders\n");
			}
			if (!validationResult.fbCheckOk) {
				messageBody.append("\t- You have too many Full Backs\n");
			}
			if (!validationResult.defCheckOk) {
				messageBody.append("\t- You have too many Defenders\n");
			}
			if (!validationResult.benchCheckOk) {
				messageBody.append("\t- You have too many on the bench.\n");
			}
		}

		messageBody.append("\nPlease check your selections.txt file and try again.  "
				+ "If it fails again, send an email to the google group and maybe if you are lucky someone will sort it out.\n\n"
				+ "DFL Manager Admin");

		message.setContent(messageBody.toString(), "text/plain");
	}

	// internal testing
	public static void main(String[] args) {
		try {
			EmailSelectionsHandler selectionHandler = new EmailSelectionsHandler();
			selectionHandler.execute();
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
