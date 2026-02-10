package net.dflmngr.model.entity;

import java.util.Comparator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflLadderPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_ladder")
@IdClass(DflLadderPK.class)
public class DflLadder implements Comparator<DflLadder>, Comparable<DflLadder> {

	@Id
	private int round;

	@Id
	@Column(name="team_code")
	private String teamCode;

	private int wins;
	private int losses;
	private int draws;

	@Column(name="points_for")
	private int pointsFor;

	@Column(name="points_against")
	private int pointsAgainst;

	@Column(name="average_for")
	private float averageFor;

	@Column(name="average_against")
	private float averageAgainst;

	private int pts;
	private float percentage;
	private boolean live;

	@Override
	public int compareTo(DflLadder o) {

		int equal = 0;
		int less = -1;
		int greater = 1;

		int pts = this.getPts();
		int oPts = o.getPts();

		if(pts > oPts) {
			return greater;
		}
		if(pts < oPts) {
			return less;
		}

		float percentage = this.getPercentage();
		float oPercentage = o.getPercentage();

		if(percentage > oPercentage) {
			return greater;
		}
		if(percentage < oPercentage) {
			return less;
		}

		int pointsFor = this.getPointsFor();
		int oPointsFor = o.getPointsFor();

		if(pointsFor > oPointsFor) {
			return greater;
		}
		if(pointsFor < oPointsFor) {
			return less;
		}

		return equal;
	}

	@Override
	public int compare(DflLadder o1, DflLadder o2) {

		int equal = 0;
		int less = -1;
		int greater = 1;

		int o1Pts = o1.getPts();
		int o2Pts = o2.getPts();

		if(o1Pts > o2Pts) {
			return greater;
		}
		if(o1Pts < o2Pts) {
			return less;
		}

		float o1Percentage = o1.getPercentage();
		float o2Percentage = o2.getPercentage();

		if(o1Percentage > o2Percentage) {
			return greater;
		}
		if(o1Percentage < o2Percentage) {
			return less;
		}

		int o1PointsFor = o1.getPointsFor();
		int o2PointsFor = o2.getPointsFor();

		if(o1PointsFor > o2PointsFor) {
			return greater;
		}
		if(o1PointsFor < o2PointsFor) {
			return less;
		}

		return equal;
	}
}
