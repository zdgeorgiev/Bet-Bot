package com.bet.manager.model.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "matches")
public class FootballMatch implements Serializable {

	private static final long serialVersionUID = -7470593573172210843L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "home_team")
	private String homeTeam;

	@Column(name = "away_team")
	private String awayTeam;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "EET")
	@Column(name = "start_date")
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "EET")
	@Column(name = "date_created")
	private Date dateCreated;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "EET")
	@Column(name = "last_modified")
	private Date lastModified;

	@OneToOne(cascade = CascadeType.ALL)
	private MatchMetaData matchMetaData;

	@Column(name = "prediction")
	private String prediction;

	@Column(name = "result")
	private String result;

	@Column(name = "winner")
	private String winner;

	@JsonIgnore
	@Column(name = "correctlyPredicted")
	private boolean correctlyPredicted;

	/**
	 * score delimiter for the result example : 4-2
	 * delimiter is "-"
	 */
	@Transient
	@JsonIgnore
	private String resultDelimiter = "-";

	public FootballMatch() {
		this.dateCreated = new Date();
		this.lastModified = new Date();
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getResult() {
		return result;
	}

	public String getResultDelimiter() {
		return resultDelimiter;
	}

	public String getWinner() {
		return winner;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setResultDelimiter(String resultDelimiter) {
		this.resultDelimiter = resultDelimiter;
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

	public MatchMetaData getMatchMetaData() {
		return matchMetaData;
	}

	public void setMatchMetaData(MatchMetaData matchMetaData) {
		this.matchMetaData = matchMetaData;
	}

	public boolean isCorrectlyPredicted() {
		return correctlyPredicted;
	}

	public void setCorrectlyPredicted(boolean correctlyPredicted) {
		this.correctlyPredicted = correctlyPredicted;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@JsonIgnore
	@Transient
	public String getSummary() {
		return String.format("'%s' - '%s' starting : %s", homeTeam, awayTeam, startDate);
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		FootballMatch that = (FootballMatch) o;

		if (homeTeam != null ? !homeTeam.equals(that.homeTeam) : that.homeTeam != null)
			return false;
		if (awayTeam != null ? !awayTeam.equals(that.awayTeam) : that.awayTeam != null)
			return false;
		if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
			return false;

		return true;

	}

	@Override public int hashCode() {
		int result1 = id;
		result1 = 31 * result1 + (homeTeam != null ? homeTeam.hashCode() : 0);
		result1 = 31 * result1 + (awayTeam != null ? awayTeam.hashCode() : 0);
		result1 = 31 * result1 + (startDate != null ? startDate.hashCode() : 0);
		return result1;
	}
}
