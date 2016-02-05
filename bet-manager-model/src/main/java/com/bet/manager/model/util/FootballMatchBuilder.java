package com.bet.manager.model.util;

import com.bet.manager.model.FootballMatch;
import com.bet.manager.model.dao.Result;
import org.jsoup.helper.StringUtil;

import java.util.Date;

public class FootballMatchBuilder {

	private FootballMatch match;

	public FootballMatchBuilder() {
		match = new FootballMatch();
	}

	public FootballMatchBuilder setHomeTeamName(String homeTeamName) {

		if (StringUtil.isBlank(homeTeamName)) {
			throw new IllegalArgumentException("Home team name cannot be empty.");
		}

		match.setHomeTeam(homeTeamName);
		return this;
	}

	public FootballMatchBuilder setAwayTeamName(String awayTeamName) {

		if (StringUtil.isBlank(awayTeamName)) {
			throw new IllegalArgumentException("Home team name cannot be empty.");
		}

		match.setAwayTeam(awayTeamName);
		return this;
	}

	public FootballMatchBuilder setStartDate(Date startDate) {

		if (startDate == null) {
			throw new IllegalArgumentException("Start date cannot be null.");
		}

		match.setStartDate(startDate);
		return this;
	}

	public FootballMatchBuilder setResult(Result result) {
		//Hmmm TODO:
		match.setResult(result);
		return this;
	}

	public FootballMatch build() {
		setHomeTeamName(match.getHomeTeam());
		setAwayTeamName(match.getAwayTeam());
		setStartDate(match.getStartDate());
		setResult(match.getResult());
		return match;
	}
}
