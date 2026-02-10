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
@Table(name = "dfl_team")
public class DflTeam {

	@Id
	@Column(name = "team_code")
	private String teamCode;
	private String name;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "coach_name")
	private String coachName;

	@Column(name = "home_ground")
	private String homeGround;

	private String colours;

	@Column(name = "coach_email")
	private String coachEmail;
}
