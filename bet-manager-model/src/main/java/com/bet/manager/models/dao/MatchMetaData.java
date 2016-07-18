package com.bet.manager.models.dao;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "match_metadata")
public class MatchMetaData implements Serializable {

	public static final int CONSTRUCTOR_PARAMS_COUNT = 32;
	public static final String CONSTRUCTOR_PARAMS_SPLITERATOR = ",";

	private static final long serialVersionUID = -7243657887791111073L;

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

	@OneToOne(cascade = CascadeType.ALL)
	private TeamMetaData homeTeamMetaData;

	@OneToOne(cascade = CascadeType.ALL)
	private TeamMetaData awayTeamMetaData;

	public MatchMetaData() {
		homeTeamMetaData = new TeamMetaData();
		awayTeamMetaData = new TeamMetaData();
	}

	public MatchMetaData(int id, String homeTeam, String awayTeam, int year, int round,
			TeamMetaData homeTeamMetaData, TeamMetaData awayTeamMetaData) {
		this.id = id;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.year = year;
		this.round = round;
		this.homeTeamMetaData = homeTeamMetaData;
		this.awayTeamMetaData = awayTeamMetaData;
	}

	public MatchMetaData(String team1, String team2, int year, int round,
			int team1position, int team1points, int team1goalDifference, int team1venue, int team1distance, int team1sprints,
			int team1passes,
			int team1shouts, int team1fouls, int team1hugeWins, int team1hugeLoses, int team1wins, int team1loses, int team1draws,
			int team2position, int team2points, int team2goalDifference, int team2venue, int team2distance, int team2sprints,
			int team2passes,
			int team2shouts, int team2fouls, int team2hugeWins, int team2hugeLoses, int team2wins, int team2loses, int team2draws) {

		this.homeTeam = team1;
		this.awayTeam = team2;
		this.year = year;
		this.round = round;

		this.homeTeamMetaData = new TeamMetaData(team1position, team1points, team1goalDifference, team1venue,
				new TeamMetaData.PreviousRoundStats(team1distance, team1sprints, team1passes, team1shouts, team1fouls),
				new TeamMetaData.LastFiveMatchesPerformance(team1hugeWins, team1hugeLoses, team1wins, team1loses, team1draws));
		this.awayTeamMetaData = new TeamMetaData(team2position, team2points, team2goalDifference, team2venue,
				new TeamMetaData.PreviousRoundStats(team2distance, team2sprints, team2passes, team2shouts, team2fouls),
				new TeamMetaData.LastFiveMatchesPerformance(team2hugeWins, team2hugeLoses, team2wins, team2loses, team2draws));
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

	@Override
	public String toString() {
		return String.format("%s %s %s", round, homeTeamMetaData, awayTeamMetaData);
	}

	@Entity
	@Table(name = "team_metadata")
	public static class TeamMetaData implements Serializable {

		private static final long serialVersionUID = 4418194598446119672L;

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "id")
		private int id;

		@Column(name = "position")
		private int position;

		@Column(name = "points")
		private int points;

		@Column(name = "goalDifference")
		private int goalDifference;

		@Column(name = "venue")
		private int venue;

		@OneToOne(cascade = CascadeType.ALL)
		private PreviousRoundStats previousRoundStats;

		@OneToOne(cascade = CascadeType.ALL)
		private LastFiveMatchesPerformance lastFiveMatchesPerformance;

		public TeamMetaData() {
			this.previousRoundStats = new PreviousRoundStats();
			lastFiveMatchesPerformance = new LastFiveMatchesPerformance();
		}

		public TeamMetaData(int position, int points, int goalDifference, int venue,
				PreviousRoundStats previousRoundStats,
				LastFiveMatchesPerformance lastFiveMatchesPerformance) {
			this.position = position;
			this.points = points;
			this.goalDifference = goalDifference;
			this.venue = venue;
			this.previousRoundStats = previousRoundStats;
			this.lastFiveMatchesPerformance = lastFiveMatchesPerformance;
		}

		public PreviousRoundStats getPreviousRoundStats() {
			return previousRoundStats;
		}

		public void setPreviousRoundStats(PreviousRoundStats previousRoundStats) {
			this.previousRoundStats = previousRoundStats;
		}

		public int getVenue() {
			return venue;
		}

		public void setVenue(int venue) {
			this.venue = venue;
		}

		public int getGoalDifference() {
			return goalDifference;
		}

		public void setGoalDifference(int goalDifference) {
			this.goalDifference = goalDifference;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public LastFiveMatchesPerformance getLastFiveMatchesPerformance() {
			return lastFiveMatchesPerformance;
		}

		public void setLastFiveMatchesPerformance(
				LastFiveMatchesPerformance lastFiveMatchesPerformance) {
			this.lastFiveMatchesPerformance = lastFiveMatchesPerformance;
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}

		@Override
		public String toString() {
			return String.format("%s %s %s %s %s %s", position, points, goalDifference, venue, previousRoundStats,
					lastFiveMatchesPerformance);
		}

		@Entity
		@Table(name = "previous_round_stats")
		public static class PreviousRoundStats implements Serializable {

			private static final long serialVersionUID = -6895618112263724187L;

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

			@Override
			public String toString() {
				return String.format("%s %s %s %s %s", distance, sprints, passes, shots, fouls);
			}
		}

		@Entity
		@Table(name = "last_five_matches_performance")
		public static class LastFiveMatchesPerformance implements Serializable {

			private static final long serialVersionUID = -6032113581519459502L;

			@Id
			@GeneratedValue(strategy = GenerationType.AUTO)
			@Column(name = "id")
			private int id;

			@Column(name = "huge_wins")
			private int hugeWins;

			@Column(name = "huge_loses")
			private int hugeLoses;

			@Column(name = "wins")
			private int wins;

			@Column(name = "loses")
			private int loses;

			@Column(name = "draws")
			private int draws;

			public LastFiveMatchesPerformance() {

			}

			public LastFiveMatchesPerformance(int hugeWins, int hugeLoses, int wins, int loses, int draws) {
				this.hugeWins = hugeWins;
				this.hugeLoses = hugeLoses;
				this.wins = wins;
				this.loses = loses;
				this.draws = draws;
			}

			public int getHugeWins() {
				return hugeWins;
			}

			public void setHugeWins(int hugeWins) {
				this.hugeWins = hugeWins;
			}

			public int getHugeLoses() {
				return hugeLoses;
			}

			public void setHugeLoses(int hugeLoses) {
				this.hugeLoses = hugeLoses;
			}

			public int getWins() {
				return wins;
			}

			public void setWins(int wins) {
				this.wins = wins;
			}

			public int getLoses() {
				return loses;
			}

			public void setLoses(int loses) {
				this.loses = loses;
			}

			public int getDraws() {
				return draws;
			}

			public void setDraws(int draws) {
				this.draws = draws;
			}

			@Override
			public String toString() {
				return String.format("%s %s %s %s %s", hugeWins, hugeLoses, wins, loses, draws);
			}
		}
	}
}
