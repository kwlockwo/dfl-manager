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
@Table(name = "afl_team")
public class AflTeam {

	@Id
	@Column(name = "team_id")
	String teamId;

	String name;
	String nickname;
	String website;

	@Column(name = "senior_uri")
	String seniorUri;

	@Column(name = "rookie_uri")
	String rookieUri;

	@Column(name = "official_website")
	String officialWebsite;

	@Column(name = "official_senior_uri")
	String officialSeniorUri;

	@Column(name = "official_rookie_uri")
	String officialRookieUri;
}
