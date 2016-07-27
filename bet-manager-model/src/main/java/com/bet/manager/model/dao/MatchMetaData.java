package com.bet.manager.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "match_metadata")
public class MatchMetaData implements Serializable {

	public static final int CONSTRUCTOR_PARAMS_COUNT = 31;
	public static final String CONSTRUCTOR_PARAMS_DELIMITER = ",";

	private static final long serialVersionUID = -7243657887791111073L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@JsonIgnore
	@Transient
	@Column(name = "home_team")
	private String homeTeam;

	@JsonIgnore
	@Transient
	@Column(name = "away_team")
	private String awayTeam;

	@OneToOne(cascade = CascadeType.ALL)
	private TeamMetaData homeTeamMetaData;

	@OneToOne(cascade = CascadeType.ALL)
	private TeamMetaData awayTeamMetaData;

	@Transient
	@JsonIgnore
	private String result;

	public MatchMetaData() {
		homeTeamMetaData = new TeamMetaData();
		awayTeamMetaData = new TeamMetaData();
	}

	public MatchMetaData(String homeTeam, String awayTeam, TeamMetaData homeTeamMetaData, TeamMetaData awayTeamMetaData,
			String result) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeTeamMetaData = homeTeamMetaData;
		this.awayTeamMetaData = awayTeamMetaData;
		this.result = result;
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

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", homeTeamMetaData, awayTeamMetaData, result);
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MatchMetaData that = (MatchMetaData) o;

		if (!homeTeam.equals(that.homeTeam))
			return false;
		if (!awayTeam.equals(that.awayTeam))
			return false;
		if (homeTeamMetaData != null ? !homeTeamMetaData.equals(that.homeTeamMetaData) : that.homeTeamMetaData != null)
			return false;
		if (awayTeamMetaData != null ? !awayTeamMetaData.equals(that.awayTeamMetaData) : that.awayTeamMetaData != null)
			return false;
		return result != null ? result.equals(that.result) : that.result == null;

	}

	@Override public int hashCode() {
		int result1 = homeTeam.hashCode();
		result1 = 31 * result1 + awayTeam.hashCode();
		result1 = 31 * result1 + (homeTeamMetaData != null ? homeTeamMetaData.hashCode() : 0);
		result1 = 31 * result1 + (awayTeamMetaData != null ? awayTeamMetaData.hashCode() : 0);
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		return result1;
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

		@JsonIgnore
		@Transient
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

		@Override public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			TeamMetaData that = (TeamMetaData) o;

			if (position != that.position)
				return false;
			if (points != that.points)
				return false;
			if (goalDifference != that.goalDifference)
				return false;
			if (venue != that.venue)
				return false;
			if (previousRoundStats != null ? !previousRoundStats.equals(that.previousRoundStats) : that.previousRoundStats != null)
				return false;
			return lastFiveMatchesPerformance != null ?
					lastFiveMatchesPerformance.equals(that.lastFiveMatchesPerformance) :
					that.lastFiveMatchesPerformance == null;

		}

		@Override public int hashCode() {
			int result = position;
			result = 31 * result + points;
			result = 31 * result + goalDifference;
			result = 31 * result + venue;
			result = 31 * result + (previousRoundStats != null ? previousRoundStats.hashCode() : 0);
			result = 31 * result + (lastFiveMatchesPerformance != null ? lastFiveMatchesPerformance.hashCode() : 0);
			return result;
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

			@Override public boolean equals(Object o) {
				if (this == o)
					return true;
				if (o == null || getClass() != o.getClass())
					return false;

				LastFiveMatchesPerformance that = (LastFiveMatchesPerformance) o;

				if (hugeWins != that.hugeWins)
					return false;
				if (hugeLoses != that.hugeLoses)
					return false;
				if (wins != that.wins)
					return false;
				if (loses != that.loses)
					return false;
				return draws == that.draws;

			}

			@Override public int hashCode() {
				int result = hugeWins;
				result = 31 * result + hugeLoses;
				result = 31 * result + wins;
				result = 31 * result + loses;
				result = 31 * result + draws;
				return result;
			}
		}
	}
}
