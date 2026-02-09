package net.dflmngr.model.web;

public class SelectedPlayer {
	
	private int playerId;
	private int teamPlayerId;
	private String name;
	private String position;
	private boolean hasPlayer;
	private boolean scoreUsed;
	private boolean isDnp;
	private String replacementInd;
	private int emgSort;
	private PlayerStats stats;
		
	public SelectedPlayer() {}

	public SelectedPlayer(int playerId, int teamPlayerId, String name, String position, boolean hasPlayer,
			boolean scoreUsed, boolean isDnp, String replacementInd, int emgSort, PlayerStats stats) {
		super();
		this.playerId = playerId;
		this.teamPlayerId = teamPlayerId;
		this.name = name;
		this.position = position;
		this.hasPlayer = hasPlayer;
		this.scoreUsed = scoreUsed;
		this.isDnp = isDnp;
		this.replacementInd = replacementInd;
		this.emgSort = emgSort;
		this.stats = stats;
	}

	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public int getTeamPlayerId() {
		return teamPlayerId;
	}
	public void setTeamPlayerId(int teamPlayerId) {
		this.teamPlayerId = teamPlayerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public boolean hasPlayer() {
		return hasPlayer;
	}
	public void setHasPlayer(boolean hasPlayer) {
		this.hasPlayer = hasPlayer;
	}
	public boolean scoreUsed() {
		return scoreUsed;
	}
	public void setScoreUsed(boolean scoreUsed) {
		this.scoreUsed = scoreUsed;
	}
	public boolean isDnp() {
		return isDnp;
	}
	public void setDnp(boolean isDnp) {
		this.isDnp = isDnp;
	}
	public String getReplacementInd() {
		return replacementInd;
	}
	public void setReplacementInd(String replacementInd) {
		this.replacementInd = replacementInd;
	}
	public int getEmgSort() {
		return emgSort;
	}
	public void setEmgSort(int emgSort) {
		this.emgSort = emgSort;
	}
	public PlayerStats getStats() {
		return stats;
	}
	public void setStats(PlayerStats stats) {
		this.stats = stats;
	}

	@Override
	public String toString() {
		return "SelectedPlayer [playerId=" + playerId + ", teamPlayerId=" + teamPlayerId + ", name=" + name
				+ ", position=" + position + ", hasPlayer=" + hasPlayer + ", scoreUsed=" + scoreUsed + ", isDnp="
				+ isDnp + ", replacementInd=" + replacementInd + ", emgSort=" + emgSort + ", stats=" + stats + "]";
	}
}
