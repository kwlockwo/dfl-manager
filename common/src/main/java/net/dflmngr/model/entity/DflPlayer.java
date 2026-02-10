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
@Table(name = "dfl_player")
public class DflPlayer {

	@Id
	@Column(name = "player_id")
	private int playerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String initial;
	private String status;

	@Column(name = "afl_club")
	private String aflClub;
	private String position;

	@Column(name = "afl_player_id")
	private String aflPlayerId;

	@Column(name = "is_first_year")
	private boolean isFirstYear;
}
