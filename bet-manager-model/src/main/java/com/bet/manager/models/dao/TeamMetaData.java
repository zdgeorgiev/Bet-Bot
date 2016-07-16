package com.bet.manager.models.dao;

import javax.persistence.*;

@Entity
@Table(name = "team-meta-data")
public class TeamMetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "position")
	private int position;
	private int goalDifference;
	private int venue;

	@JoinTable
	private PreviousRoundStats previousRoundStats;

	public TeamMetaData() {
		this.previousRoundStats = new PreviousRoundStats();
	}

	public TeamMetaData(int currentPosition, int goalDifference, int venue, PreviousRoundStats previousRoundStats) {
		this.position = currentPosition;
		this.goalDifference = goalDifference;
		this.venue = venue;
		this.previousRoundStats = previousRoundStats;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getGoalDifference() {
		return goalDifference;
	}

	public void setGoalDifference(int goalDifference) {
		this.goalDifference = goalDifference;
	}

	public int getVenue() {
		return venue;
	}

	public void setVenue(int venue) {
		this.venue = venue;
	}

	public PreviousRoundStats getPreviousRoundStats() {
		return previousRoundStats;
	}

	public void setPreviousRoundStats(PreviousRoundStats previousRoundStats) {
		this.previousRoundStats = previousRoundStats;
	}
}