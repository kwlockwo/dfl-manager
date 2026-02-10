package net.dflmngr.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dfl_round_mapping")
public class DflRoundMapping {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="round")
	private int round;

	@Column(name="afl_round")
	private int aflRound;

	@Column(name="afl_game")
	private int aflGame;

	@Column(name="afl_team")
	private String aflTeam;
}
