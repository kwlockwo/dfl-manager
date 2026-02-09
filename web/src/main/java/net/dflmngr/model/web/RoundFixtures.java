package net.dflmngr.model.web;

import java.util.List;

public class RoundFixtures {
	
	private int round;
	private List<GameFixture> games;
	
	public RoundFixtures() {}
	
	public RoundFixtures(int round, List<GameFixture> games) {
		this.round = round;
		this.games = games;
	}
	
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public List<GameFixture> getGames() {
		return games;
	}
	public void setGames(List<GameFixture> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "RoundFixtures [round=" + round + ", games=" + games + "]";
	}
}
