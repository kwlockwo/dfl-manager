package net.dflmngr.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflPreseasonScoresPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_preseason_scores")
@IdClass(DflPreseasonScoresPK.class)
public class DflPreseasonScores {

	@Id
	@Column(name="player_id")
	private int playerId;

	@Id
	private int round;

	private int score;
}
