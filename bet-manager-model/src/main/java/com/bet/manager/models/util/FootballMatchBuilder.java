package com.bet.manager.models.util;

import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.exceptions.EmptyTeamNameException;
import com.bet.manager.models.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.models.exceptions.InvalidMatchDateException;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class FootballMatchBuilder {

	private FootballMatch match;

	public FootballMatchBuilder() {
		match = new FootballMatch();
	}

	public FootballMatchBuilder(FootballMatch match) {
		this.match = match;
	}

	public FootballMatchBuilder setHomeTeamName(String homeTeamName) {

		if (StringUtils.isBlank(homeTeamName)) {
			throw new EmptyTeamNameException("Home team name cannot be empty.");
		}

		match.setHomeTeam(homeTeamName);
		return this;
	}

	public FootballMatchBuilder setAwayTeamName(String awayTeamName) {

		if (StringUtils.isBlank(awayTeamName)) {
			throw new EmptyTeamNameException("Home team name cannot be empty.");
		}

		match.setAwayTeam(awayTeamName);
		return this;
	}

	public FootballMatchBuilder setStartDate(Date startDate) {

		if (startDate == null) {
			throw new InvalidMatchDateException("FootballMatch start date cannot be null.");
		}

		match.setStartDate(startDate);
		return this;
	}

	public FootballMatchBuilder setResult(String result) {

		match.setResult(result);
		FootballMatchUtils.setResultAndWinner(match, match.getResult());

		return this;
	}

	public FootballMatch build() {
		setHomeTeamName(match.getHomeTeam());
		setAwayTeamName(match.getAwayTeam());

		if (match.getHomeTeam().equals(match.getAwayTeam())) {
			throw new EqualHomeAndAwayTeamException(
					"FootballMatch home team " + match.getHomeTeam()
							+ " cannot be the same as the away team.");
		}

		setStartDate(match.getStartDate());
		setResult(match.getResult());
		return match;
	}
}
