package com.bet.manager.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class FootballMatch implements Serializable {

	private static final long serialVersionUID = -7470593573172210843L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@JsonIgnore
	private Long id;

	@Column(name = "home_team")
	private String homeTeam;

	@Column(name = "away_team")
	private String awayTeam;

	@Column(name = "year")
	private int year;

	@Column(name = "round")
	private int round;

	@Column(name = "start_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "EET")
	private LocalDateTime startDate;

	@JsonIgnore
	@Column(name = "date_created")
	private LocalDateTime dateCreated;

	@JsonIgnore
	@Column(name = "last_modified")
	private LocalDateTime lastModified;

	@Column(name = "matchStatus")
	private MatchStatus matchStatus;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "metadata_id")
	private MatchMetaData matchMetaData;

	@Column(name = "result")
	private String result;

	@Column(name = "winner")
	private String winner;

	@Column(name = "prediction")
	private String prediction;

	@Column(name = "predictionType")
	private PredictionType predictionType;

	public FootballMatch() {
		this.dateCreated = LocalDateTime.now();
		this.lastModified = LocalDateTime.now();
		predictionType = PredictionType.NOT_PREDICTED;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public MatchStatus getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(MatchStatus matchStatus) {
		this.matchStatus = matchStatus;
	}

	public MatchMetaData getMatchMetaData() {
		return matchMetaData;
	}

	public void setMatchMetaData(MatchMetaData matchMetaData) {
		this.matchMetaData = matchMetaData;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public PredictionType getPredictionType() {
		return predictionType;
	}

	public void setPredictionType(PredictionType predictionType) {
		this.predictionType = predictionType;
	}

	@JsonIgnore
	@Transient
	public String getSummary() {
		return String.format("['%s' - '%s' %s round %s]", homeTeam, awayTeam, year, round);
	}

	@JsonIgnore
	@Transient
	public String getMetaDataNNInput() {
		return String.format("%s%s%s", round, MatchMetaData.SPLITERATOR, matchMetaData);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof FootballMatch))
			return false;

		FootballMatch that = (FootballMatch) o;

		if (year != that.year)
			return false;
		if (round != that.round)
			return false;
		if (!homeTeam.equals(that.homeTeam))
			return false;
		if (!awayTeam.equals(that.awayTeam))
			return false;
		if (matchStatus != that.matchStatus)
			return false;
		if (matchMetaData != null ? !matchMetaData.equals(that.matchMetaData) : that.matchMetaData != null)
			return false;
		if (result != null ? !result.equals(that.result) : that.result != null)
			return false;
		return prediction != null ? prediction.equals(that.prediction) : that.prediction == null;
	}

	@Override
	public int hashCode() {
		int result1 = homeTeam.hashCode();
		result1 = 31 * result1 + awayTeam.hashCode();
		result1 = 31 * result1 + year;
		result1 = 31 * result1 + round;
		result1 = 31 * result1 + (matchStatus != null ? matchStatus.hashCode() : 0);
		result1 = 31 * result1 + (matchMetaData != null ? matchMetaData.hashCode() : 0);
		result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
		result1 = 31 * result1 + (prediction != null ? prediction.hashCode() : 0);
		return result1;
	}

	@Override public String toString() {
		return "FootballMatch [" +
				"year=" + year +
				", round=" + round +
				", startDate=" + startDate +
				", homeTeam='" + homeTeam + '\'' +
				", awayTeam='" + awayTeam + '\'' +
				" ]";
	}
}
