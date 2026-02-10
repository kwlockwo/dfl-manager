package net.dflmngr.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflTeamPredictedScoresPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dfl_team_predicted_scores")
@IdClass(DflTeamPredictedScoresPK.class)
public class DflTeamPredictedScores {

	@Id
	@Column(name = "team_code")
	private String teamCode;

	@Id
	private int round;

	@Column(name = "predicted_score")
	private int predictedScore;
}
