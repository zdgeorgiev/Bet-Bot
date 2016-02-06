package com.bet.manager.model.dao;

import com.bet.manager.commons.ResultMessages;
import org.apache.commons.lang.StringUtils;

public abstract class Result {

	/**
	 * score in format specified in your sub class
	 */
	protected String score;

	/**
	 * winnerCode of the current match
	 * 1 - home team wins
	 * 2 - away team wins
	 * 0 - tie
	 * -1 - unknown
	 */
	protected int winnerCode;

	/**
	 * scoreDelimiter that is used in the score
	 * example: 2-2 (scoreDelimiter is - )
	 */
	protected String scoreDelimiter;

	public String getScore() {
		return score;
	}

	public String getScoreDelimiter() {
		return scoreDelimiter;
	}

	public int getWinnerCode() {
		return winnerCode;
	}

	public void setWinnerCode(int winnerCode) {
		this.winnerCode = winnerCode;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return StringUtils.isBlank(score) ? ResultMessages.UNKNOWN_WINNER : score;
	}
}
