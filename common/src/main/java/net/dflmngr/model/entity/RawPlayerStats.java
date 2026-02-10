package net.dflmngr.model.entity;

import java.io.Serializable;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import net.dflmngr.model.entity.keys.RawPlayerStatsPK;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="raw_player_stats")
@IdClass(RawPlayerStatsPK.class)
@Cacheable(false)
public class RawPlayerStats implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int round;
	@Id
	private String name;
	@Id
	String team;

	@Id
	@Column(name="jumper_no")
	private int jumperNo;

	private int kicks;
	private int handballs;
	private int disposals;
	private int marks;
	private int hitouts;

	@Column(name="frees_for")
	private int freesFor;

	@Column(name="frees_against")
	private int freesAgainst;

	private int tackles;
	private int goals;
	private int behinds;

	@Column(name="scraping_status")
	private String scrapingStatus;
}
