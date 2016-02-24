package com.bet.manager.models.dao;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;

import java.util.Date;

public abstract class Match {

	/**
	 * name of the home team
	 */
	protected String homeTeam;

	/**
	 * name of the away team
	 */
	protected String awayTeam;

	/**
	 * Date when the match will start
	 */
	protected Date startDate;

	/**
	 * Result for the current match
	 */
	protected String result;

	/**
	 * score delimiter for the result example : 4-2
	 * delimiter is "-"
	 */
	protected String resultDelimiter;

	/**
	 * winner in the current match
	 */
	protected String winner;

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
				.format("Match { [%s] %s %s - %s }",
						DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(getStartDate()),
						getResult() == null ? ResultMessages.UNKNOWN_SCORE : getResult(),
						getHomeTeam(),
						getAwayTeam());
	}
}
