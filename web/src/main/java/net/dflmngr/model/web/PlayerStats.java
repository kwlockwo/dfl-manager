package net.dflmngr.model.web;

public class PlayerStats {
	
	private int kicks;
	private int handballs;
	private int disposals;
	private int marks;
	private int hitouts;
	private int freesFor;
	private int freesAgainst;
	private int tackles;
	private int goals;
	private int behinds;
	private int score;
	private int predictedScore;
	private int trend;
	private String scrapingStatus;
	
	public PlayerStats() {}
	
	public PlayerStats(int kicks, int handballs, int disposals, int marks, int hitouts, int freesFor, int freesAgainst, 
			           int tackles, int goals, int behinds, int score, int predictedScore, int trend, String scrapingStatus) {
		this.kicks = kicks;
		this.handballs = handballs;
		this.disposals = disposals;
		this.marks = marks;
		this.hitouts = hitouts;
		this.freesFor = freesFor;
		this.freesAgainst = freesAgainst;
		this.tackles = tackles;
		this.goals = goals;
		this.behinds = behinds;
		this.score = score;
		this.predictedScore = predictedScore;
		this.trend = trend;
		this.scrapingStatus = scrapingStatus;
	}

	public int getKicks() {
		return kicks;
	}

	public void setKicks(int kicks) {
		this.kicks = kicks;
	}

	public int getHandballs() {
		return handballs;
	}

	public void setHandballs(int handballs) {
		this.handballs = handballs;
	}

	public int getDisposals() {
		return disposals;
	}

	public void setDisposals(int disposals) {
		this.disposals = disposals;
	}

	public int getMarks() {
		return marks;
	}

	public void setMarks(int marks) {
		this.marks = marks;
	}

	public int getHitouts() {
		return hitouts;
	}

	public void setHitouts(int hitouts) {
		this.hitouts = hitouts;
	}

	public int getFreesFor() {
		return freesFor;
	}

	public void setFreesFor(int freesFor) {
		this.freesFor = freesFor;
	}

	public int getFreesAgainst() {
		return freesAgainst;
	}

	public void setFreesAgainst(int freesAgainst) {
		this.freesAgainst = freesAgainst;
	}

	public int getTackles() {
		return tackles;
	}

	public void setTackles(int tackles) {
		this.tackles = tackles;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getBehinds() {
		return behinds;
	}

	public void setBehinds(int behinds) {
		this.behinds = behinds;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public String getScrapingStatus() {
		return scrapingStatus;
	}

	public void setScrapingStatus(String scrapingStatus) {
		this.scrapingStatus = scrapingStatus;
	}

	@Override
	public String toString() {
		return "PlayerStats [kicks=" + kicks + ", handballs=" + handballs + ", disposals=" + disposals + ", marks="
				+ marks + ", hitouts=" + hitouts + ", freesFor=" + freesFor + ", freesAgainst=" + freesAgainst
				+ ", tackles=" + tackles + ", goals=" + goals + ", behinds=" + behinds + ", score=" + score
				+ ", predictedScore=" + predictedScore + ", trend=" + trend + ", scrapingStatus=" + scrapingStatus
				+ "]";
	}
}
