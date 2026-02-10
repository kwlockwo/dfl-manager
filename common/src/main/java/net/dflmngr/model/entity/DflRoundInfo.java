package net.dflmngr.model.entity;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dfl_round_info")
public class DflRoundInfo {

	@Id
	@Column(name = "round")
	private int round;

	@Column(name = "hard_lockout")
	//@Temporal(TemporalType.TIMESTAMP)
	private ZonedDateTime hardLockoutTime;

	@Column(name = "split_round")
	private String splitRound;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="round")
	private List<DflRoundEarlyGames> earlyGames;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="round")
	private List<DflRoundMapping> roundMapping;
}
