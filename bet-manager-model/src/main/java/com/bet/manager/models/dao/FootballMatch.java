package com.bet.manager.models.dao;

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

	@Column(name = "start_date")
	private Date startDate;

	@OneToOne(cascade = CascadeType.ALL)
	private MatchMetaData matchMetaData;

	@Column(name = "prediction")
	private String prediction;

	@Column(name = "result")
	private String result;

	@Column(name = "winner")
	private String winner;

	/**
	 * score delimiter for the result example : 4-2
	 * delimiter is "-"
	 */
	@Transient
	@JsonIgnore
	private String resultDelimiter = "-";

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
}
