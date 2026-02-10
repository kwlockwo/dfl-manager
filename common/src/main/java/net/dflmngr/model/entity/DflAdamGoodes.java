package net.dflmngr.model.entity;

import java.util.Comparator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DflAdamGoodes implements Comparator<DflAdamGoodes>, Comparable<DflAdamGoodes> {

	private int round;
	private int playerId;
	private String teamCode;
	private int teamPlayerId;
	private int totalScore;

	@Override
	public int compareTo(DflAdamGoodes o) {
		return this.totalScore > o.totalScore ? 1 : (this.totalScore < o.totalScore ? -1 : 0);
	}

	@Override
	public int compare(DflAdamGoodes o1, DflAdamGoodes o2) {
		return o1.totalScore > o2.totalScore ? 1 : (o2.totalScore < o2.totalScore ? -1 : 0);
	}
}
