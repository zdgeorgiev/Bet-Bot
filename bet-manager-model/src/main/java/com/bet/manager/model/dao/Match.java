package com.bet.manager.model.dao;

import com.bet.manager.commons.DateFormats;

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
	private Date startDate;

	/**
	 * Result for the current match
	 */
	private Result result;

	/**
	 * winner in the current match
	 */
	private String winner;

	public String getHomeTeam() {
		return homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Result getResult() {
		return result;
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

	public void setResult(Result result) {
		this.result = result;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	@Override public String toString() {
		return String
				.format("Match { [%s] %s %s - %s }",
						DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(getStartDate()),
						getResult().toString(),
						getHomeTeam(),
						getAwayTeam());
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Match match = (Match) o;

		if (homeTeam != null ? !homeTeam.equals(match.homeTeam) : match.homeTeam != null)
			return false;
		if (awayTeam != null ? !awayTeam.equals(match.awayTeam) : match.awayTeam != null)
			return false;
		return startDate != null ? startDate.equals(match.startDate) : match.startDate == null;

	}

	@Override public int hashCode() {
		int result = homeTeam != null ? homeTeam.hashCode() : 0;
		result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
		result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
		return result;
	}
}
