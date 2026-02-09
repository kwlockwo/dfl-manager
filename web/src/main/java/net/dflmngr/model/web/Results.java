package net.dflmngr.model.web;

public class Results {
	
	private int round;
	private int game;
	private TeamResults homeTeam;
	private TeamResults awayTeam;
	
	public Results() {}
	
	public Results(int round, int game, TeamResults homeTeam, TeamResults awayTeam) {
		this.round = round;
		this.game = game;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getGame() {
		return game;
	}

	public void setGame(int game) {
		this.game = game;
	}

	public TeamResults getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(TeamResults homeTeam) {
		this.homeTeam = homeTeam;
	}

	public TeamResults getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(TeamResults awayTeam) {
		this.awayTeam = awayTeam;
	}

	@Override
	public String toString() {
		return "Results [round=" + round + ", game=" + game + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + "]";
	}
}
