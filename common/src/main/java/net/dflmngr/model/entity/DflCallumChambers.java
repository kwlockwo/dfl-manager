package net.dflmngr.model.entity;

import java.util.Comparator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DflCallumChambers implements Comparator<DflCallumChambers>, Comparable<DflCallumChambers> {

	private int round;
	private int playerId;
	private String teamCode;
	private int draftOrder;
	private int teamPlayerId;
	private int totalScore;

	@Override
	public int compareTo(DflCallumChambers o) {
		int equal = 0;
		int less = -1;
		int greater = 1;

		int totalScore = this.getTotalScore();
		int oTotalScore = o.getTotalScore();

		if(totalScore < oTotalScore) {
			return greater;
		}
		if(totalScore > oTotalScore) {
			return less;
		}

		int teamPlayerId = this.getTeamPlayerId();
		int oTeamPlayerId = o.getTeamPlayerId();

		if(teamPlayerId < oTeamPlayerId) {
			return greater;
		}
		if(teamPlayerId > oTeamPlayerId) {
			return less;
		}

		int draftOrder = this.getDraftOrder();
		int oDraftOrder = o.getDraftOrder();

		if(draftOrder > oDraftOrder) {
			return greater;
		}
		if(draftOrder < oDraftOrder) {
			return less;
		}

		return equal;
	}

	@Override
	public int compare(DflCallumChambers o1, DflCallumChambers o2) {
		int equal = 0;
		int less = -1;
		int greater = 1;

		int o1TotalScore = o1.getTotalScore();
		int o2TotalScore = o2.getTotalScore();

		if(o1TotalScore < o2TotalScore) {
			return greater;
		}
		if(o1TotalScore > o2TotalScore) {
			return less;
		}

		int o1teamPlayerId = o1.getTeamPlayerId();
		int o2TeamPlayerId = o2.getTeamPlayerId();

		if(o1teamPlayerId < o2TeamPlayerId) {
			return greater;
		}
		if(o1teamPlayerId > o2TeamPlayerId) {
			return less;
		}

		int o1draftOrder = o1.getDraftOrder();
		int o2DraftOrder = o2.getDraftOrder();

		if(o1draftOrder > o2DraftOrder) {
			return greater;
		}
		if(o1draftOrder < o2DraftOrder) {
			return less;
		}

		return equal;
	}
}
