package com.bet.manager.web.model;

public class MatchMetaData {

	private String label;
	private int year;
	private int round;
	private TeamMetaData firstTeamMetaData;
	private TeamMetaData secondTeamMetaData;

	public MatchMetaData(String label, int year, int round, TeamMetaData firstTeamMetaData, TeamMetaData secondTeamMetaData) {
		this.label = label;
		this.year = year;
		this.round = round;
		this.firstTeamMetaData = firstTeamMetaData;
		this.secondTeamMetaData = secondTeamMetaData;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public TeamMetaData getFirstTeamMetaData() {
		return firstTeamMetaData;
	}

	public void setFirstTeamMetaData(TeamMetaData firstTeamMetaData) {
		this.firstTeamMetaData = firstTeamMetaData;
	}

	public TeamMetaData getSecondTeamMetaData() {
		return secondTeamMetaData;
	}

	public void setSecondTeamMetaData(TeamMetaData secondTeamMetaData) {
		this.secondTeamMetaData = secondTeamMetaData;
	}
}
