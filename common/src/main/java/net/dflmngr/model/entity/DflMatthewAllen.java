package net.dflmngr.model.entity;

import java.util.Comparator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dfl_matthew_allen")
public class DflMatthewAllen implements Comparator<DflMatthewAllen>, Comparable<DflMatthewAllen> {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	private int round;
	private int game;

	@Column(name="player_id")
	private int playerId;

	private int score;
	private int votes;
	private int total;

	@Override
	public int compareTo(DflMatthewAllen o) {
		if(this.getTotal() > o.getTotal()) {
			return 1;
		}
		return this.getTotal() < o.getTotal() ? -1 : 0;
	}

	@Override
	public int compare(DflMatthewAllen o1, DflMatthewAllen o2) {
		if(o1.getTotal() > o2.getTotal()) {
			return 1;
		}
		return o1.getTotal() < o2.getTotal() ? -1 : 0;
	}
}
