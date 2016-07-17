package com.bet.manager.models.dao;

import javax.persistence.*;

@Entity
public class PreviousRoundStats {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "distance")
	private int distance;

	@Column(name = "sprints")
	private int sprints;

	@Column(name = "passes")
	private int passes;

	@Column(name = "shots")
	private int shots;

	@Column(name = "fouls")
	private int fouls;

	public PreviousRoundStats() {
	}

	public PreviousRoundStats(int trackDistance, int sprintsCount, int passesCount, int shotsCount, int foulsCount) {
		this.distance = trackDistance;
		this.sprints = sprintsCount;
		this.passes = passesCount;
		this.shots = shotsCount;
		this.fouls = foulsCount;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSprints() {
		return sprints;
	}

	public void setSprints(int sprints) {
		this.sprints = sprints;
	}

	public int getPasses() {
		return passes;
	}

	public void setPasses(int passes) {
		this.passes = passes;
	}

	public int getShots() {
		return shots;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}
}