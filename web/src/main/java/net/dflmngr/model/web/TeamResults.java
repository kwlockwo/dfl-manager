package net.dflmngr.model.web;

import java.util.List;

public class TeamResults {

	private String teamCode;
	private String teamName;
	private List<SelectedPlayer> players;
	private List<SelectedPlayer> emergencies;
	private int score;
	private int currentPredictedScore;
	private int predictedScore;
	private int trend;
	private String emgInd;
	
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public List<SelectedPlayer> getPlayers() {
		return players;
	}
	public void setPlayers(List<SelectedPlayer> players) {
		this.players = players;
	}
	public List<SelectedPlayer> getEmergencies() {
		return emergencies;
	}
	public void setEmergencies(List<SelectedPlayer> emergencies) {
		this.emergencies = emergencies;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCurrentPredictedScore() {
		return currentPredictedScore;
	}
	public void setCurrentPredictedScore(int currentPredictedScore) {
		this.currentPredictedScore = currentPredictedScore;
	}

	public int getPredictedScore() {
		return predictedScore;
	}
	public void setPredictedScore(int predictedScore) {
		this.predictedScore = predictedScore;
	}
	public int getTrend() {
		return trend;
	}
	public void setTrend(int trend) {
		this.trend = trend;
	}
	public String getEmgInd() {
		return emgInd;
	}
	public void setEmgInd(String emgInd) {
		this.emgInd = emgInd;
	}

	@Override
	public String toString() {
		return "TeamResults [teamCode=" + teamCode + ", teamName=" + teamName + ", players=" + players
				+ ", emergencies=" + emergencies + ", score=" + score + ", currentPredictedScore="
				+ currentPredictedScore + ", predictedScore=" + predictedScore + ", trend=" + trend + ", emgInd="
				+ emgInd + "]";
	}
}
