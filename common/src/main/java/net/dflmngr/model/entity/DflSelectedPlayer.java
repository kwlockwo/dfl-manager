package net.dflmngr.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflSelectedPlayerPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_selected_player")
@IdClass(DflSelectedPlayerPK.class)
public class DflSelectedPlayer {

	@Id
	private int round;

	@Id
	@Column(name = "player_id")
	private int playerId;

	@Column(name = "team_player_id")
	private int teamPlayerId;

	@Column(name = "team_code")
	private String teamCode;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Column(name = "is_emergency")
	private int isEmergency;

	// Custom accessors to maintain backward compatibility
	public int isEmergency() {
		return isEmergency;
	}

	public void setEmergency(int isEmergency) {
		this.isEmergency = isEmergency;
	}

	@Column(name = "is_dnp")
	private boolean isDnp;

	@Column(name = "score_used")
	private boolean scoreUsed;

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	@Column(name = "has_played")
	private boolean hasPlayed;

	@Column(name = "replacement_ind")
	private String replacementInd;

	// Custom accessors to maintain backward compatibility
	public boolean hasPlayed() {
		return hasPlayed;
	}

	public void setHasPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}
}
