package net.dflmngr.validation;

import java.util.List;
import java.util.Map;

import net.dflmngr.model.entity.DflPlayer;

public class SelectedTeamValidation {
	
	public boolean earlyGames;
	public boolean playedSelections;
	public boolean selectionFileMissing;
	public boolean lockedOut;
	public boolean roundCompleted;
	
	public boolean ffCheckOk;
	public boolean fwdCheckOk;
	public boolean rckCheckOk;
	public boolean midCheckOk;
	public boolean fbCheckOk;
	public boolean defCheckOk;
	public boolean benchCheckOk;
	public boolean emergencyCheckOk;

	public boolean teamPlayerCheckOk;
	
	public boolean emergencyWarning;
	
	public boolean duplicateSubmissionId;
	
	public boolean selectedWarning;
	public boolean droppedWarning;
	public List<DflPlayer> selectedWarnPlayers;
	public List<DflPlayer> droppedWarnPlayers;
	
	public boolean emptyTeam;
	public boolean unknownError;
	
	private int round;
	private String teamCode;
	
	private String from;
	
	private Map<String, List<Integer>> insAndOuts;
	private List<Integer> emergencies;
	
	public List<DflPlayer> ffPlayers;
	public List<DflPlayer> fwdPlayers;
	public List<DflPlayer> midPlayers;
	public List<DflPlayer> defPlayers;
	public List<DflPlayer> fbPlayers;
	public List<DflPlayer> rckPlayers;
	public List<DflPlayer> benchPlayers;
	
	public boolean duplicateIns;
	public boolean duplicateOuts;
	public boolean duplicateEmgs;
	public List<DflPlayer> dupInPlayers;
	public List<DflPlayer> dupOutPlayers;
	public List<DflPlayer> dupEmgPlayers;
	
	public SelectedTeamValidation() {
		earlyGames = false;
		playedSelections = false;
		selectionFileMissing = true;
		lockedOut = true;
		roundCompleted = true;
		
		ffCheckOk = false;
		fwdCheckOk = false;
		rckCheckOk = false;
		midCheckOk = false;
		fbCheckOk = false;
		defCheckOk = false;
		benchCheckOk = false;
		emergencyCheckOk = false;

		teamPlayerCheckOk = false;
		
		emergencyWarning = false;
		
		duplicateSubmissionId = false;
		
		emptyTeam = false;
		unknownError = false;
	}
	
	public boolean isValid() {
		
		boolean valid = false;
		
		if(earlyGames) {
			if(!playedSelections) {
				valid = true;
			}
		} else {
			if(!selectionFileMissing && !emptyTeam && ffCheckOk && fwdCheckOk && rckCheckOk && midCheckOk && fbCheckOk && defCheckOk && benchCheckOk
				&& emergencyCheckOk && teamPlayerCheckOk && !unknownError && !lockedOut && !roundCompleted && !duplicateSubmissionId) {
				valid = true;
			}
		}
		
		return valid;
	}
	
	public boolean areWarnings() {
		
		boolean warnings = false;
		
		if(emergencyWarning || selectedWarning || droppedWarning || duplicateIns || duplicateOuts || duplicateEmgs) {
			warnings = true;
		}
		
		return warnings;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getTeamCode() {
		return teamCode;
	}

	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}

	public Map<String, List<Integer>> getInsAndOuts() {
		return insAndOuts;
	}

	public void setInsAndOuts(Map<String, List<Integer>> insAndOuts) {
		this.insAndOuts = insAndOuts;
	}
	
	public List<Integer> getEmergencies() {
		return emergencies;
	}

	public void setEmergencies(List<Integer> emergencies) {
		this.emergencies = emergencies;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
