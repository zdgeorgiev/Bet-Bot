package com.bet.manager.models.dao;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
public abstract class FootballMatch {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected int id;

	@Column(name = "home_team")
	protected String homeTeam;

	@Column(name = "away_team")
	protected String awayTeam;

	@Column(name = "start_date")
	protected Date startDate;

	@Column(name = "result")
	protected String result;

	@Column(name = "winner")
	protected String winner;

	/**
	 * score delimiter for the result example : 4-2
	 * delimiter is "-"
	 */
	@Transient
	protected String resultDelimiter;

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

	@Override public String toString() {
		return String
				.format("FootballMatch { [%s] %s %s - %s }",
						DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(getStartDate()),
						getResult() == null ? ResultMessages.UNKNOWN_SCORE : getResult(),
						getHomeTeam(),
						getAwayTeam());
	}
}
