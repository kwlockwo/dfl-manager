package net.dflmngr.model.entity;

import java.util.Comparator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflPlayerPredictedScoresPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_player_predicted_scores")
@IdClass(DflPlayerPredictedScoresPK.class)
public class DflPlayerPredictedScores implements Comparator<DflPlayerPredictedScores>, Comparable<DflPlayerPredictedScores> {

	@Id
	@Column(name="player_id")
	private int playerId;

	@Id
	private int round;

	@Column(name="afl_player_id")
	private String aflPlayerId;

	@Column(name="team_code")
	private String teamCode;

	@Column(name="team_player_id")
	private int teamPlayerId;

	@Column(name="predicted_score")
	private int predictedScore;

	@Override
	public int compareTo(DflPlayerPredictedScores o) {
		return this.predictedScore > o.predictedScore ? 1 : (this.predictedScore < o.predictedScore ? -1 : 0);
	}

	@Override
	public int compare(DflPlayerPredictedScores o1, DflPlayerPredictedScores o2) {
		return o1.predictedScore > o2.predictedScore ? 1 : (o2.predictedScore < o2.predictedScore ? -1 : 0);
	}
}
