package com.bet.manager.models.dao;

import javax.persistence.*;

@Entity
@Table(name = "metadata")
public class MatchMetaData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "home_team")
	private String homeTeam;

	@Column(name = "away_team")
	private String awayTeam;

	@Column(name = "year")
	private int year;

	@Column(name = "round")
	private int round;

	@OneToOne
	@JoinTable(name = "home_team_meta_data")
	private TeamMetaData homeTeamMetaData;

	@OneToOne
	@JoinTable(name = "away_team_meta_data")
	private TeamMetaData awayTeamMetaData;

	public MatchMetaData() {
		homeTeamMetaData = new TeamMetaData();
		awayTeamMetaData = new TeamMetaData();
	}

	public MatchMetaData(String team1, String team2, int year, int round,
			int team1position, int team1goalDifference, int team1venue, int team1distance, int team1sprints, int team1passes,
			int team1shouts, int team1fouls,
			int team2position, int team2goalDifference, int team2venue, int team2distance, int team2sprints, int team2passes,
			int team2shouts, int team2fouls) {

		this.homeTeam = team1;
		this.awayTeam = team2;
		this.year = year;
		this.round = round;

		this.homeTeamMetaData = new TeamMetaData(team1position, team1goalDifference, team1venue,
				new PreviousRoundStats(team1distance, team1sprints, team1passes, team1shouts, team1fouls));
		this.awayTeamMetaData = new TeamMetaData(team2position, team2goalDifference, team2venue,
				new PreviousRoundStats(team2distance, team2sprints, team2passes, team2shouts, team2fouls));
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
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

	public TeamMetaData getHomeTeamMetaData() {
		return homeTeamMetaData;
	}

	public void setHomeTeamMetaData(TeamMetaData homeTeamMetaData) {
		this.homeTeamMetaData = homeTeamMetaData;
	}

	public TeamMetaData getAwayTeamMetaData() {
		return awayTeamMetaData;
	}

	public void setAwayTeamMetaData(TeamMetaData awayTeamMetaData) {
		this.awayTeamMetaData = awayTeamMetaData;
	}

}
