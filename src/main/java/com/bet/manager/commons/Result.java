package com.bet.manager.commons;

import org.jsoup.helper.StringUtil;

public abstract class Result {

	/**
	 * score in format specified in your sub class
	 */
	protected String score;

	/**
	 * name of the home team
	 */
	protected final String homeTeam;

	/**
	 * name of the away team
	 */
	protected final String awayTeam;

	/**
	 * delimiter that is used in the score
	 * example: 2-2 (delimiter is - )
	 */
	protected final String delimiter;

	public Result(String homeTeam, String awayTeam, String delimiter) {
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.delimiter = delimiter;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {

		if (StringUtil.isBlank(score)) {
			return ResultMessages.UNKNOWN_RESULT;
		}

		return score;
	}

	public String getWinner() {

		if (getScore().equals(ResultMessages.UNKNOWN_RESULT)) {
			return ResultMessages.UNKNOWN_WINNER;
		}

		String[] tokens = this.score.split(this.delimiter);
		Integer homeTeamGoals = Integer.parseInt(tokens[0]);
		Integer awayTeamGoals = Integer.parseInt(tokens[1]);

		if (homeTeamGoals > awayTeamGoals) {
			return homeTeam;
		} else if (homeTeamGoals < awayTeamGoals) {
			return awayTeam;
		} else {
			return ResultMessages.TIE_RESULT;
		}
	}
}
