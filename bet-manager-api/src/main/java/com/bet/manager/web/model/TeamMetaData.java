package com.bet.manager.web.model;

public class TeamMetaData {

	private int currentPosition;
	private int goalDifference;
	private int venue;

	private PreviousRoundStats previousRoundStats;

	public TeamMetaData(int currentPosition, int goalDifference, int venue,
			PreviousRoundStats previousRoundStats) {
		this.currentPosition = currentPosition;
		this.goalDifference = goalDifference;
		this.venue = venue;
		this.previousRoundStats = previousRoundStats;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
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
