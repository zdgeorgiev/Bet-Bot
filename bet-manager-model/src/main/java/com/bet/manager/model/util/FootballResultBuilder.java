package com.bet.manager.model.util;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.FootballResult;
import org.apache.commons.lang.StringUtils;

public class FootballResultBuilder {

	private FootballResult result;

	public FootballResultBuilder() {
		result = new FootballResult();
	}

	public FootballResultBuilder setScore(String score) {

		if (StringUtils.isBlank(score) || score.matches("d - d")) {
			result.setScore(ResultMessages.UNKNOWN_WINNER);
		} else {
			result.setScore(score);
		}

		return this;
	}

	public FootballResult build() {
		setScore(result.getScore());
		setWinner();
		return result;
	}

	private FootballResultBuilder setWinner() {

		if (result.getScore().equals(ResultMessages.UNKNOWN_WINNER)) {
			result.setWinnerCode(-1);
		} else {
			String[] tokens = result.getScore().split(result.getScoreDelimiter());
			Integer homeTeamGoals = Integer.parseInt(tokens[0].trim());
			Integer awayTeamGoals = Integer.parseInt(tokens[1].trim());

			if (homeTeamGoals > awayTeamGoals) {
				result.setWinnerCode(1);
			} else if (homeTeamGoals < awayTeamGoals) {
				result.setWinnerCode(2);
			} else {
				result.setWinnerCode(0);
			}
		}

		return this;
	}
}
