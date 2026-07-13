package net.dflmngr.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.DflPlayer;
import net.dflmngr.model.entity.DflRoundInfo;
import net.dflmngr.model.entity.DflRoundMapping;
import net.dflmngr.model.entity.DflSelectedPlayer;
import net.dflmngr.model.entity.DflSelectionIds;
import net.dflmngr.model.entity.DflTeamPlayer;
import net.dflmngr.model.service.AflFixtureService;
import net.dflmngr.model.service.DflEarlyInsAndOutsService;
import net.dflmngr.model.service.DflPlayerService;
import net.dflmngr.model.service.DflRoundInfoService;
import net.dflmngr.model.service.DflSelectedTeamService;
import net.dflmngr.model.service.DflSelectionIdsService;
import net.dflmngr.model.service.DflTeamPlayerService;
import net.dflmngr.model.service.GlobalsService;
import net.dflmngr.validation.SelectedTeamValidation;

public class SelectedTeamValidationHandler extends BaseHandler {

	private DflSelectedTeamService dflSelectedTeamService;
	private DflTeamPlayerService dflTeamPlayerService;
	private DflPlayerService dflPlayerService;
	private GlobalsService globalsService;
	private DflRoundInfoService dflRoundInfoService;
	private DflEarlyInsAndOutsService dflEarlyInsAndOutsService;
	private AflFixtureService aflFixtureService;
	private DflSelectionIdsService dflSelectionIdsService;

	public SelectedTeamValidationHandler() {
		super("SelectedTeamValidationHandler");
		dflSelectedTeamService = serviceFactory.createDflSelectedTeamService();
		dflTeamPlayerService = serviceFactory.createDflTeamPlayerService();
		dflPlayerService = serviceFactory.createDflPlayerService();
		globalsService = serviceFactory.createGlobalsService();
		dflRoundInfoService = serviceFactory.createDflRoundInfoService();
		dflEarlyInsAndOutsService = serviceFactory.createDflEarlyInsAndOutsService();
		aflFixtureService = serviceFactory.createAflFixtureService();
		dflSelectionIdsService = serviceFactory.createDflSelectionIdsService();
	}

	public SelectedTeamValidation execute(int round, String teamCode, Map<String, List<Integer>> insAndOuts, List<Double> emergencies, String selectionId) {

		SelectedTeamValidation validationResult = null;

		try {
			ensureLoggingConfigured();

			loggerUtils.log("info", "Validating selectionds for teamCode={}; round={};", teamCode, round);

			int currentRound = Integer.parseInt(globalsService.getCurrentRound());

			validationResult = standardValidation(round, currentRound, teamCode, insAndOuts, emergencies, selectionId);
			validationResult.setRound(round);
			validationResult.setTeamCode(teamCode);

			if(validationResult.getInsAndOuts() == null) {
				validationResult.setInsAndOuts(insAndOuts);
			}
			if(validationResult.getEmergencies() == null) {
				validationResult.setEmergencies(emergencies);
			}

			loggerUtils.log("info", "Validation result={}", validationResult);

		} catch (Exception ex) {
			loggerUtils.logException("Error in SelectedTeamValidationHandler.execute(), round=" + round + " teamCode=" + teamCode, ex);
			if(validationResult == null) {
				validationResult = new SelectedTeamValidation();
				validationResult.unknownError = true;
				validationResult.selectionFileMissing = false;
				validationResult.roundCompleted = false;
				validationResult.lockedOut = false;
				validationResult.setRound(round);
				validationResult.setTeamCode(teamCode);
			}
		} finally {
			dflSelectedTeamService.close();
			dflTeamPlayerService.close();
			dflPlayerService.close();
			globalsService.close();
			dflRoundInfoService.close();
			dflEarlyInsAndOutsService.close();
			aflFixtureService.close();
			dflSelectionIdsService.close();
		}

		return validationResult;
	}

	private SelectedTeamValidation standardValidation(int round, int currentRound, String teamCode, Map<String, List<Integer>> insAndOuts, List<Double> emergencies, String selectionId) {

		SelectedTeamValidation validationResult = new SelectedTeamValidation();

		loggerUtils.log("info", "DFL round={};", currentRound);

		List<DflSelectedPlayer> selectedTeam = null;

		validationResult.selectedWarning = false;
		validationResult.droppedWarning = false;

		List<Integer> checkedIns = new ArrayList<>();
		List<Integer> checkedOuts = new ArrayList<>();
		List<Double> checkedEmgs = new ArrayList<>();
		boolean playerNumbersOk = true;

		List<DflPlayer> selectedWarnPlayers = new ArrayList<>();
		List<DflPlayer> droppedWarnPlayers = new ArrayList<>();

		List<DflPlayer> dupInPlayers = new ArrayList<>();
		List<DflPlayer> dupOutPlayers = new ArrayList<>();
		List<DflPlayer> dupEmgPlayers = new ArrayList<>();

		DflRoundInfo roundInfo = dflRoundInfoService.get(round);
		List<Integer> aflRounds = new ArrayList<>();

		for(DflRoundMapping mapping : roundInfo.getRoundMapping()) {
			if(!aflRounds.contains(mapping.getAflRound())) {
				aflRounds.add(mapping.getAflRound());
			}
		}

		boolean aflRoundComplete = true;
		for(int aflRound : aflRounds) {
			aflRoundComplete = aflRoundComplete && aflFixtureService.getAflRoundComplete(aflRound);
		}

		boolean selectionExists = dflSelectionIdsService.selectionIdExists(round, teamCode, selectionId);

		if(round < currentRound) {
			validationResult.selectionFileMissing = false;
			validationResult.roundCompleted = true;
			loggerUtils.log("info", "Team invalid round is completed");
		} else if(aflRoundComplete) {
			validationResult.selectionFileMissing = false;
			validationResult.roundCompleted = false;
			validationResult.lockedOut = true;
			loggerUtils.log("info", "Team invalid email recived after AFL round complete");
		} else if(selectionExists) {
			validationResult.selectionFileMissing = false;
			validationResult.roundCompleted = false;
			validationResult.lockedOut = false;
			validationResult.duplicateSubmissionId = true;
			loggerUtils.log("info", "Already handled a selection for round={}. teamCode={}, id={}", round, teamCode, selectionId);
		} else {
			if(round == 1) {
				loggerUtils.log("info", "Round 1 only ins.");
				List<Integer> ins = insAndOuts.get("in");

				selectedTeam = new ArrayList<>();

				for(int in : ins) {
					if(in < 1 || in > 45) {
						loggerUtils.log("info", "Selected player outside player range, teamPlayerId={}.", in);
						playerNumbersOk = false;
						break;
					}

					DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(teamCode, in);
					if(teamPlayer == null) {
						loggerUtils.log("info", "Selected player does not exist, teamPlayerId={}.", in);
						playerNumbersOk = false;
						break;
					}
					DflPlayer player = dflPlayerService.get(teamPlayer.getPlayerId());

					if(checkedIns.contains(in)) {
						loggerUtils.log("info", "Duplicates ins, not included in={}.", in);
						validationResult.duplicateIns = true;
						dupInPlayers.add(player);
					} else {
						DflSelectedPlayer selectedPlayer = new DflSelectedPlayer();

						selectedPlayer.setPlayerId(player.getPlayerId());
						selectedPlayer.setRound(round);
						selectedPlayer.setTeamCode(teamCode);
						selectedPlayer.setTeamPlayerId(in);
						selectedPlayer.setEmergency(0);
						selectedPlayer.setDnp(false);

						selectedTeam.add(selectedPlayer);
						loggerUtils.log("info", "Added selectedPlayer={}.", selectedPlayer);

						checkedIns.add(in);
					}
				}

				for(Double emg : emergencies) {
					int emergency = emg.intValue();

					if(emergency < 1 || emergency > 45) {
						loggerUtils.log("info", "Emergency player outside player range, teamPlayerId={}.", emergency);
						playerNumbersOk = false;
						break;
					}

					DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(teamCode, emergency);
					if(teamPlayer == null) {
						loggerUtils.log("info", "Emergency player does not exist, teamPlayerId={}.", emergency);
						playerNumbersOk = false;
						break;
					}
					DflPlayer player = dflPlayerService.get(teamPlayer.getPlayerId());

					if(checkedEmgs.contains(emg) || checkedIns.contains(emergency)) {
						loggerUtils.log("info", "Duplicate emgergency, not included in={}.", emergency);
						validationResult.duplicateEmgs = true;
						dupEmgPlayers.add(player);
					} else {
						DflSelectedPlayer selectedPlayer = new DflSelectedPlayer();

						selectedPlayer.setPlayerId(player.getPlayerId());
						selectedPlayer.setRound(round);
						selectedPlayer.setTeamCode(teamCode);
						selectedPlayer.setTeamPlayerId(emergency);

						int e1e2 = Integer.parseInt(Double.toString(emg).split("\\.")[1].substring(0, 1));
						if(e1e2 == 1) {
							selectedPlayer.setEmergency(1);
						} else {
							selectedPlayer.setEmergency(2);
						}

						selectedPlayer.setDnp(false);

						selectedTeam.add(selectedPlayer);
						loggerUtils.log("info", "Added selectedPlayer={}, as emergency", selectedPlayer);

						checkedEmgs.add(emg);
					}
				}
			} else {
				selectedTeam = dflSelectedTeamService.getSelectedTeamForRound(round-1, teamCode);

				loggerUtils.log("info", "Removeing emergencies from previous round");
				List<DflSelectedPlayer> playersToRemove = new ArrayList<>();
				for(DflSelectedPlayer selectedPlayer : selectedTeam) {
					if(selectedPlayer.isEmergency() != 0) {
						playersToRemove.add(selectedPlayer);
						loggerUtils.log("info", "Removing emergency={}", selectedPlayer);
					}
				}
				selectedTeam.removeAll(playersToRemove);
				playersToRemove.clear();

				List<Integer> ins = insAndOuts.get("in");
				List<Integer> outs = insAndOuts.get("out");

				for(int in : ins) {
					if(in < 1 || in > 45) {
						loggerUtils.log("info", "Selected player outside player range, teamPlayerId={}.", in);
						playerNumbersOk = false;
						break;
					}

					DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(teamCode, in);
					if(teamPlayer == null) {
						loggerUtils.log("info", "Selected player does not exist, teamPlayerId={}.", in);
						playerNumbersOk = false;
						break;
					}
					DflPlayer player = dflPlayerService.get(teamPlayer.getPlayerId());

					if(checkedIns.contains(in)) {
						loggerUtils.log("info", "Duplicates ins, not included in={}.", in);
						validationResult.duplicateIns = true;
						dupInPlayers.add(player);
					} else {
						boolean found = false;
						boolean isEmg = false;
						for(DflSelectedPlayer selectedPlayer : selectedTeam) {
							if(in == selectedPlayer.getTeamPlayerId()) {
								found = true;
								if(selectedPlayer.isEmergency() == 1 || selectedPlayer.isEmergency() == 2) {
									isEmg = true;
								}
								break;
							}
						}
						if(!found) {
							DflSelectedPlayer selectedPlayer = new DflSelectedPlayer();

							selectedPlayer.setPlayerId(player.getPlayerId());
							selectedPlayer.setRound(round);
							selectedPlayer.setTeamCode(teamCode);
							selectedPlayer.setTeamPlayerId(in);
							selectedPlayer.setEmergency(0);
							selectedPlayer.setDnp(false);

							selectedTeam.add(selectedPlayer);
							loggerUtils.log("info", "Added selectedPlayer={}.", selectedPlayer);
						} else {
							if(!isEmg) {
								loggerUtils.log("info", "Player already selected, teamPlayerId={}.", in);
								selectedWarnPlayers.add(player);
								validationResult.selectedWarning = true;
							}
						}

						checkedIns.add(in);
					}
				}

				for(int out : outs) {
					if(out < 1 || out > 45) {
						loggerUtils.log("info", "Dropped player outside player range, teamPlayerId={}.", out);
						playerNumbersOk = false;
						break;
					}

					DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(teamCode, out);
					if(teamPlayer == null) {
						loggerUtils.log("info", "Dropped player does not exist, teamPlayerId={}.", out);
						playerNumbersOk = false;
						break;
					}
					DflPlayer player = dflPlayerService.get(teamPlayer.getPlayerId());

					if(checkedOuts.contains(out)) {
						loggerUtils.log("info", "Duplicates out, not included in={}.", out);
						validationResult.duplicateOuts = true;
						dupOutPlayers.add(player);
					} else {
						boolean found = false;
						for(DflSelectedPlayer selectedPlayer : selectedTeam) {
							if(selectedPlayer.getTeamPlayerId() == out) {
								playersToRemove.add(selectedPlayer);
								found = true;
								loggerUtils.log("info", "Removing selectedPlayer={}.", selectedPlayer);
							}
						}
						if(!found) {
							loggerUtils.log("info", "Dropped player not selected, teamPlayerId={}.", out);
							droppedWarnPlayers.add(player);
							validationResult.droppedWarning = true;
						}

						checkedOuts.add(out);
					}
				}

				for(Double emg : emergencies) {
					int emergency = emg.intValue();

					if(emergency < 1 || emergency > 45) {
						loggerUtils.log("info", "Emergency player outside player range, teamPlayerId={}.", emergency);
						playerNumbersOk = false;
						break;
					}

					DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(teamCode, emergency);
					if(teamPlayer == null) {
						loggerUtils.log("info", "Emergency player does not exist, teamPlayerId={}.", emergency);
						playerNumbersOk = false;
						break;
					}
					DflPlayer player = dflPlayerService.get(teamPlayer.getPlayerId());

					if(checkedEmgs.contains(emg) || checkedIns.contains(emergency)) {
						loggerUtils.log("info", "Duplicate emgergency, not included emg={}.", emergency);
						validationResult.duplicateEmgs = true;
						dupEmgPlayers.add(player);
					} else {
						boolean alreadySelected = false;
						for(DflSelectedPlayer selectedPlayer : selectedTeam) {
							if(emergency == selectedPlayer.getTeamPlayerId()) {
								alreadySelected = true;
								break;
							}
						}
						if(alreadySelected) {
							loggerUtils.log("info", "Emergency emg={}.", emergency);
						} else {
							DflSelectedPlayer selectedPlayer = new DflSelectedPlayer();

							selectedPlayer.setPlayerId(player.getPlayerId());
							selectedPlayer.setRound(round);
							selectedPlayer.setTeamCode(teamCode);
							selectedPlayer.setTeamPlayerId(emergency);

							int e1e2 = Integer.parseInt(Double.toString(emg).split("\\.")[1].substring(0, 1));
							if(e1e2 == 1) {
								selectedPlayer.setEmergency(1);
							} else {
								selectedPlayer.setEmergency(2);
							}

							selectedPlayer.setDnp(false);

							selectedTeam.add(selectedPlayer);
							loggerUtils.log("info", "Added selectedPlayer={}.", selectedPlayer);
						}

						checkedEmgs.add(emg);
					}
				}

				selectedTeam.removeAll(playersToRemove);
			}

			if(!playerNumbersOk) {
				validationResult.selectionFileMissing = false;
				validationResult.roundCompleted = false;
				validationResult.lockedOut = false;
				validationResult.teamPlayerCheckOk = false;
				loggerUtils.log("info", "Pre checks FAILED, invalid player numbers");
				return validationResult;
			}

			loggerUtils.log("info", "Pre checks PASSED, validating selected team");

			insAndOuts.replace("in", checkedIns);
			insAndOuts.replace("out", checkedOuts);

			validationResult.setInsAndOuts(insAndOuts);
			validationResult.setEmergencies(checkedEmgs);

			if(validationResult.selectedWarning) {
				validationResult.selectedWarnPlayers = selectedWarnPlayers;
			}
			if(validationResult.droppedWarning) {
				validationResult.droppedWarnPlayers = droppedWarnPlayers;
			}
			if(validationResult.duplicateIns) {
				validationResult.dupInPlayers = dupInPlayers;
			}
			if(validationResult.duplicateOuts) {
				validationResult.dupOutPlayers = dupOutPlayers;
			}
			if(validationResult.duplicateEmgs) {
				validationResult.dupEmgPlayers = dupEmgPlayers;
			}

			if(selectedTeam.isEmpty()) {
				loggerUtils.log("info", "Selected team is empty after applying ins and outs");
				validationResult.emptyTeam = true;
				return validationResult;
			}

			validationResult = validateTeam(selectedTeam, validationResult);

			if(!selectionId.equalsIgnoreCase("noid")) {
				DflSelectionIds selectionIds = new DflSelectionIds();
				selectionIds.setRound(round);
				selectionIds.setTeamCode(teamCode);
				selectionIds.setSelectionId(selectionId);

				dflSelectionIdsService.insert(selectionIds);
			}
		}

		return validationResult;
	}

	private SelectedTeamValidation validateTeam(List<DflSelectedPlayer> selectedTeam, SelectedTeamValidation validationResult) {

		validationResult.selectionFileMissing = false;
		validationResult.roundCompleted = false;
		validationResult.lockedOut = false;
		validationResult.teamPlayerCheckOk = true;

		PositionCounts positionCounts = new PositionCounts();
		Map<String, List<DflPlayer>> playersByPosition = new HashMap<>();

		List<String> emergencyPositions = new ArrayList<>();
		List<DflPlayer> emgPlayers = new ArrayList<>();

		for(DflSelectedPlayer selectedPlayer : selectedTeam) {
			DflTeamPlayer teamPlayer = dflTeamPlayerService.getTeamPlayerForTeam(selectedPlayer.getTeamCode(), selectedPlayer.getTeamPlayerId());
			DflPlayer player = teamPlayer != null ? dflPlayerService.get(teamPlayer.getPlayerId()) : null;

			if(player == null) {
				loggerUtils.log("warn", "No player found for teamCode={} teamPlayerId={}, skipping position checks",
								selectedPlayer.getTeamCode(), selectedPlayer.getTeamPlayerId());
				continue;
			}

			String position = player.getPosition().toLowerCase();

			if(selectedPlayer.isEmergency() == 0) {
				positionCounts.increment(position);
				playersByPosition.computeIfAbsent(position, k -> new ArrayList<>()).add(player);
			} else {
				emergencyPositions.add(position);
				emgPlayers.add(player);
			}
		}

		loggerUtils.log("info", "Position counts: {}, emergencyPositions={};", positionCounts, emergencyPositions);

		if(!positionCounts.overLimit("ff")) {
			validationResult.ffCheckOk = true;
			validationResult.ffPlayers = playersByPosition.getOrDefault("ff", new ArrayList<>());
		}
		if(!positionCounts.overLimit("fwd")) {
			validationResult.fwdCheckOk = true;
			validationResult.fwdPlayers = playersByPosition.getOrDefault("fwd", new ArrayList<>());
		}
		if(!positionCounts.overLimit("mid")) {
			validationResult.midCheckOk = true;
			validationResult.midPlayers = playersByPosition.getOrDefault("mid", new ArrayList<>());
		}
		if(!positionCounts.overLimit("def")) {
			validationResult.defCheckOk = true;
			validationResult.defPlayers = playersByPosition.getOrDefault("def", new ArrayList<>());
		}
		if(!positionCounts.overLimit("fb")) {
			validationResult.fbCheckOk = true;
			validationResult.fbPlayers = playersByPosition.getOrDefault("fb", new ArrayList<>());
		}
		if(!positionCounts.overLimit("rck")) {
			validationResult.rckCheckOk = true;
			validationResult.rckPlayers = playersByPosition.getOrDefault("rck", new ArrayList<>());
		}

		int benchCount = positionCounts.benchCount();

		loggerUtils.log("info", "Bench count={};", benchCount);

		if(benchCount <= 5) {
			validationResult.benchCheckOk = true;
		}

		for(String position : emergencyPositions) {
			positionCounts.increment(position);
		}

		if(positionCounts.anyOverLimit()) {
			List<Double> emgs = validationResult.getEmergencies();
			for(DflPlayer player : emgPlayers) {
				String pos = player.getPosition().toLowerCase();

				if(positionCounts.overLimit(pos)) {
					validationResult.emergencyWarning = true;
					loggerUtils.log("info", "Invalid emergency, player={};", player);

					DflTeamPlayer teamPlayer = dflTeamPlayerService.get(player.getPlayerId());
					int emg1 = emgs.size() >= 1 ? emgs.get(0).intValue() : 0;

					if(emg1 == teamPlayer.getTeamPlayerId()) {
						emgs.remove(0);
						loggerUtils.log("info", "Removed emergency, player={};", teamPlayer);
					}
				}
			}
			validationResult.setEmergencies(emgs);
		}

		return validationResult;
	}
}
