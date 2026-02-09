package net.dflmngr.model.web;

import java.util.List;

public class RoundMenu {
	
	private int round;
	private List<GameMenu> games;
	private boolean active;
	
	public RoundMenu() {}
	
	public RoundMenu(int round, List<GameMenu> games, boolean active) {
		this.round = round;
		this.games = games;
		this.active = active;
	}
	
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public List<GameMenu> getGames() {
		return games;
	}
	public void setGames(List<GameMenu> games) {
		this.games = games;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "RoundMenu [round=" + round + ", games=" + games + "]";
	}
}
