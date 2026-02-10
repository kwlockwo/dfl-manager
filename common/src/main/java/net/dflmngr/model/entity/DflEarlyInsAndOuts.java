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
@Table(name = "dfl_early_ins_and_outs")
public class DflEarlyInsAndOuts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private Integer id;

	@Column(name = "team_code")
	private String teamCode;

	@Column(name = "round")
	private int round;

	@Column(name = "team_player_id")
	private int teamPlayerId;

	@Column(name = "in_or_out")
	private String inOrOut;
}
