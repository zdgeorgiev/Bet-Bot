package com.bet.manager.model.util;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.FootballMatch;
import com.bet.manager.model.dao.Result;
import com.bet.manager.model.exceptions.EmptyTeamNameException;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.model.exceptions.InvalidMatchDateException;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class FootballMatchBuilder {

	private FootballMatch match;

	public FootballMatchBuilder() {
		match = new FootballMatch();
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
			throw new InvalidMatchDateException("Match start date cannot be null.");
		}

		match.setStartDate(startDate);
		return this;
	}

	public FootballMatchBuilder setResult(Result result) {
		match.setResult(result);
		return this;
	}

	public FootballMatch build() {
		setHomeTeamName(match.getHomeTeam());
		setAwayTeamName(match.getAwayTeam());

		if (match.getHomeTeam().equals(match.getAwayTeam())) {
			throw new EqualHomeAndAwayTeamException(
					"Match home team " + match.getHomeTeam() + " cannot be the same as the away team.");
		}

		setStartDate(match.getStartDate());
		setResult(match.getResult());
		setWinner();
		return match;
	}

	private void setWinner() {
		if (match.getResult() == null) {
			match.setWinner(ResultMessages.NO_WINNER);
			return;
		}

		int winnerCode = match.getResult().getWinnerCode();

		switch (winnerCode) {
		case 1:
			match.setWinner(match.getHomeTeam());
			break;
		case 2:
			match.setWinner(match.getAwayTeam());
			break;
		case 0:
			match.setWinner(ResultMessages.NO_WINNER);
			break;
		case -1:
			match.setWinner(ResultMessages.NO_WINNER);
			break;
		default:
			throw new IllegalStateException("Invalid winner code " + winnerCode);
		}
	}
}
