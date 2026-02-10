package net.dflmngr.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.DflFixturePK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dfl_fixture")
@IdClass(DflFixturePK.class)
public class DflFixture {

	@Id
	private int round;

	@Id
	private int game;

	@Column(name="home_team")
	private String homeTeam;

	@Column(name="away_team")
	private String awayTeam;
}
