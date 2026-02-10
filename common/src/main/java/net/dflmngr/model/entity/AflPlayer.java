package net.dflmngr.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "afl_player")
public class AflPlayer {

	@Id
	@Column(name = "player_id")
	private String playerId;

	@Column(name = "jumper_no")
	private int jumperNo;

	@Column(name = "name")
	private String name;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "second_name")
	private String secondName;

	@Column(name = "team_id")
	private String teamId;

	private int height;
	private int weight;
	private Date dob;

	@Column(name = "dfl_player_id")
	private int dflPlayerId;
}
