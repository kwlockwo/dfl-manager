package net.dflmngr.model.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import jakarta.persistence.*;

import net.dflmngr.model.entity.keys.AflFixturePK;

import java.util.Comparator;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="afl_fixture")
@IdClass(AflFixturePK.class)
public class AflFixture implements Comparator<AflFixture>, Comparable<AflFixture>, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int round;

	@Id
	private int game;

	@Column(name="away_team")
	private String awayTeam;

	@Column(name="home_team")
	private String homeTeam;

	@Column
	private String ground;

	@Column(name="start_time")
	private ZonedDateTime startTime;

	@Column
	private String timezone;

	@Column(name="end_time")
	private ZonedDateTime endTime;

	@Column(name="stats_downloaded")
	private boolean statsDownloaded;

	@Override
	public int compareTo(AflFixture in) {
		Integer gameRound = Integer.parseInt(Integer.toString(this.round) + Integer.toString(this.game));
		Integer inGameRound = Integer.parseInt(Integer.toString(in.round) + Integer.toString(in.game));

		return gameRound.compareTo(inGameRound);
	}

	@Override
	public int compare(AflFixture a1, AflFixture a2) {
		Integer a1gameRound = Integer.parseInt(Integer.toString(a1.round) + Integer.toString(a1.game));
		Integer a2gameRound = Integer.parseInt(Integer.toString(a2.round) + Integer.toString(a2.game));

		return a1gameRound - a2gameRound;
	}
}
