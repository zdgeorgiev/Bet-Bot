package com.bet.manager.model.dao;

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
	private Long id;

	@Column(name = "home_team")
	private String homeTeam;

	@Column(name = "away_team")
	private String awayTeam;

	@Column(name = "year")
	private int year;

	@Column(name = "round")
	private int round;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "EET")
	@Column(name = "start_date")
	private LocalDateTime startDate;

	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "EET")
	@Column(name = "date_created")
	private LocalDateTime dateCreated;

	@JsonIgnore
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "EET")
	@Column(name = "last_modified")
	private LocalDateTime lastModified;

	@Column(name = "matchStatus")
	private MatchStatus matchStatus;

	@OneToOne(cascade = CascadeType.PERSIST)
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

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
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

		return true;
	}

	@Override public int hashCode() {
		int result = homeTeam.hashCode();
		result = 31 * result + awayTeam.hashCode();
		result = 31 * result + year;
		result = 31 * result + round;
		return result;
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
