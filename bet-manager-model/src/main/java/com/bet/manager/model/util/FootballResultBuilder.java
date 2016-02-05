package com.bet.manager.model.util;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.FootballResult;
import org.jsoup.helper.StringUtil;

public class FootballResultBuilder {

	private FootballResult result;

	public FootballResultBuilder() {
		result = new FootballResult();
	}

	public FootballResultBuilder setHomeTeam(String homeTeam) {

		if (StringUtil.isBlank(homeTeam)) {
			throw new IllegalArgumentException("Home team name cannot be empty");
		}

		result.setHomeTeam(homeTeam);
		return this;
	}

	public FootballResultBuilder setAwayTeam(String awayTeam) {

		if (StringUtil.isBlank(awayTeam)) {
			throw new IllegalArgumentException("Away team name cannot be empty");
		}

		result.setAwayTeam(awayTeam);
		return this;
	}

	public FootballResultBuilder setResultDelimiter(String scoreDelimiter) {

		if (StringUtil.isBlank(scoreDelimiter)) {
			throw new IllegalArgumentException("Score delimiter cannot be empty");
		}

		result.setScoreDelimiter(scoreDelimiter);
		return this;
	}

	public FootballResultBuilder setScore(String score) {

		if (StringUtil.isBlank(score)) {
			result.setScore(ResultMessages.UNKNOWN_SCORE);
		} else {
			result.setScore(score);
		}

		return this;
	}

	public FootballResult build() {
		setHomeTeam(result.getHomeTeam());
		setAwayTeam(result.getAwayTeam());
		setResultDelimiter(result.getScoreDelimiter());
		setScore(result.getScore());
		setWinner();
		return result;
	}

	private FootballResultBuilder setWinner() {

		if (result.getScore().equals(ResultMessages.UNKNOWN_SCORE)) {
			result.setWinner(ResultMessages.UNKNOWN_WINNER);
		} else {
			String[] tokens = result.getScore().split(result.getScoreDelimiter());
			Integer homeTeamGoals = Integer.parseInt(tokens[0]);
			Integer awayTeamGoals = Integer.parseInt(tokens[1]);

			if (homeTeamGoals > awayTeamGoals) {
				result.setWinner(result.getHomeTeam());
			} else if (homeTeamGoals < awayTeamGoals) {
				result.setWinner(result.getAwayTeam());
			} else {
				result.setWinner(ResultMessages.TIE_SCORE);
			}
		}

		return this;
	}
}
