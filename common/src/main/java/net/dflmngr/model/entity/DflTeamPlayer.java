package net.dflmngr.model.entity;

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
@Table(name = "dfl_team_player")
public class DflTeamPlayer {

	@Id
	@Column(name = "player_id")
	private int playerId;

	@Column(name = "team_code")
	private String teamCode;

	@Column(name = "team_player_id")
	private int teamPlayerId;
}
