package com.bet.manager.web.model;

public class PreviousRoundStats {

	private int trackDistance;
	private int sprintsCount;
	private int passesCount;
	private int shotsCount;
	private int foulsCount;

	public PreviousRoundStats() {
	}

	public PreviousRoundStats(int trackDistance, int sprintsCount, int passesCount, int shotsCount, int foulsCount) {
		this.trackDistance = trackDistance;
		this.sprintsCount = sprintsCount;
		this.passesCount = passesCount;
		this.shotsCount = shotsCount;
		this.foulsCount = foulsCount;
	}

	public int getTrackDistance() {
		return trackDistance;
	}

	public void setTrackDistance(int trackDistance) {
		this.trackDistance = trackDistance;
	}

	public int getSprintsCount() {
		return sprintsCount;
	}

	public void setSprintsCount(int sprintsCount) {
		this.sprintsCount = sprintsCount;
	}

	public int getPassesCount() {
		return passesCount;
	}

	public void setPassesCount(int passesCount) {
		this.passesCount = passesCount;
	}

	public int getShotsCount() {
		return shotsCount;
	}

	public void setShotsCount(int shotsCount) {
		this.shotsCount = shotsCount;
	}

	public int getFoulsCount() {
		return foulsCount;
	}

	public void setFoulsCount(int foulsCount) {
		this.foulsCount = foulsCount;
	}
}
