package net.dflmngr.model.web;

public class Ladder {

	private int round;
	private String teamCode;
	private int wins;
	private int losses;
	private int draws;	
	private int pointsFor;
	private int pointsAgainst;
	private float averageFor;
	private float averageAgainst;
	private int pts;
	private float percentage;
	private String displayName;
	private String teamUri;
	
	public Ladder() {}

	public Ladder(int round, String teamCode, int wins, int losses, int draws, int pointsFor, int pointsAgainst,
			float averageFor, float averageAgainst, int pts, float percentage, String displayName, String teamUri) {
		this.round = round;
		this.teamCode = teamCode;
		this.wins = wins;
		this.losses = losses;
		this.draws = draws;
		this.pointsFor = pointsFor;
		this.pointsAgainst = pointsAgainst;
		this.averageFor = averageFor;
		this.averageAgainst = averageAgainst;
		this.pts = pts;
		this.percentage = percentage;
		this.displayName = displayName;
		this.teamUri = teamUri;
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

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getPointsFor() {
		return pointsFor;
	}

	public void setPointsFor(int pointsFor) {
		this.pointsFor = pointsFor;
	}

	public int getPointsAgainst() {
		return pointsAgainst;
	}

	public void setPointsAgainst(int pointsAgainst) {
		this.pointsAgainst = pointsAgainst;
	}

	public float getAverageFor() {
		return averageFor;
	}

	public void setAverageFor(float averageFor) {
		this.averageFor = averageFor;
	}

	public float getAverageAgainst() {
		return averageAgainst;
	}

	public void setAverageAgainst(float averageAgainst) {
		this.averageAgainst = averageAgainst;
	}

	public int getPts() {
		return pts;
	}

	public void setPts(int pts) {
		this.pts = pts;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTeamUri() {
		return teamUri;
	}

	public void setTeamUri(String teamUri) {
		this.teamUri = teamUri;
	}

	@Override
	public String toString() {
		return "Ladder [round=" + round + ", teamCode=" + teamCode + ", wins=" + wins + ", losses=" + losses
				+ ", draws=" + draws + ", pointsFor=" + pointsFor + ", pointsAgainst=" + pointsAgainst + ", averageFor="
				+ averageFor + ", averageAgainst=" + averageAgainst + ", pts=" + pts + ", percentage=" + percentage
				+ ", displayName=" + displayName + ", teamUri=" + teamUri + "]";
	}
}
