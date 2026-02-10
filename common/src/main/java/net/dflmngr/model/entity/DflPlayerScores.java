package net.dflmngr.model.entity;

import java.util.Comparator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflPlayerScoresPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_player_scores")
@IdClass(DflPlayerScoresPK.class)
public class DflPlayerScores implements Comparator<DflPlayerScores>, Comparable<DflPlayerScores> {

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
	private int score;

	@Override
	public int compareTo(DflPlayerScores o) {
		return this.score > o.score ? 1 : (this.score < o.score ? -1 : 0);
	}

	@Override
	public int compare(DflPlayerScores o1, DflPlayerScores o2) {
		return o1.score > o2.score ? 1 : (o2.score < o2.score ? -1 : 0);
	}
}
