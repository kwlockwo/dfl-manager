package net.dflmngr.model.web;

public class GameMenu {

	int game;
	String homeTeam;
	String awayTeam;
	boolean active;
	String resultsUri;
	
	public GameMenu(){}
	
	public GameMenu(int game, String homeTeam, String awayTeam, boolean active, String resultsUri) {
		this.game = game;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.active = active;
		this.resultsUri = resultsUri;
	}

	public int getGame() {
		return game;
	}
	public void setGame(int game) {
		this.game = game;
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getAwayTeam() {
		return awayTeam;
	}
	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getResultsUri() {
		return resultsUri;
	}
	public void setResultsUri(String resultsUri) {
		this.resultsUri = resultsUri;
	}

	@Override
	public String toString() {
		return "GameMenu [game=" + game + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + ", active=" + active
				+ ", resultsUri=" + resultsUri + "]";
	}
}
