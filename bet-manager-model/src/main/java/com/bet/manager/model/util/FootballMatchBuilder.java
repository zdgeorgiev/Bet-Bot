package com.bet.manager.model.util;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.dao.PredictionType;
import com.bet.manager.model.exceptions.EmptyTeamNameException;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class FootballMatchBuilder {

	private static Logger logger = LoggerFactory.getLogger(FootballMatchBuilder.class);

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
			logger.warn("Star date for the match " + match.getSummary() + " is not present. Check if everything is fine.");
		}

		match.setStartDate(startDate);
		return this;
	}

	public FootballMatchBuilder setResult(String result) {

		if (StringUtils.isBlank(result)) {
			match.setResult(ResultMessages.UNKNOWN_RESULT);
		} else {
			match.setResult(result.replace(" ", ""));
		}

		return this;
	}

	private FootballMatchBuilder setWinner() {

		if (!match.getResult().contains(match.getResultDelimiter())) {
			throw new IllegalArgumentException(
					"Result " + match.getResult() + " not contains the delimiter " + match.getResultDelimiter());
		}

		String winner;

		if (match.getResult().equals(ResultMessages.UNKNOWN_RESULT)) {
			winner = ResultMessages.UNKNOWN_WINNER;
		} else {
			winner = getWinnerFromResult(match, match.getResult());
		}

		match.setWinner(winner);
		return this;
	}

	private void setFinished() {
		if (!match.getWinner().equals(ResultMessages.UNKNOWN_WINNER))
			match.setFinished(true);
	}

	private String getWinnerFromResult(FootballMatch match, String result) {
		String winner;
		String[] tokens = result.split(match.getResultDelimiter());
		Integer homeTeamGoals = Integer.parseInt(tokens[0].trim());
		Integer awayTeamGoals = Integer.parseInt(tokens[1].trim());

		if (homeTeamGoals < 0 || awayTeamGoals < 0) {
			throw new IllegalArgumentException("Goals in the result cannot be less than zero.");
		}

		if (homeTeamGoals > awayTeamGoals) {
			winner = match.getHomeTeam();
		} else if (homeTeamGoals < awayTeamGoals) {
			winner = match.getAwayTeam();
		} else {
			winner = ResultMessages.TIE_RESULT;
		}
		return winner;
	}

	public FootballMatchBuilder setMatchMetaData(MatchMetaData matchMetaData) {
		match.setMatchMetaData(matchMetaData);
		return this;
	}

	public FootballMatchBuilder setPrediction(String prediction) {
		match.setPrediction(prediction);
		return this;
	}

	private void setCorrectlyPredicted() {

		if (match.getWinner() == null || match.getPrediction() == null) {
			return;
		}

		if (match.getPrediction().equals(match.getWinner())) {
			match.setPredictionType(PredictionType.CORRECT);
		} else
			match.setPredictionType(PredictionType.INCORRECT);
	}

	public FootballMatchBuilder setYear(int year) {
		this.match.setYear(year);
		return this;
	}

	public FootballMatchBuilder setRound(int round) {
		this.match.setRound(round);
		return this;
	}

	private void setLastModified() {
		match.setLastModified(new Date());
	}

	public FootballMatch build() {
		setHomeTeamName(match.getHomeTeam());
		setAwayTeamName(match.getAwayTeam());

		if (match.getHomeTeam().equals(match.getAwayTeam())) {
			throw new EqualHomeAndAwayTeamException(
					"FootballMatch home team " + match.getHomeTeam() + " cannot be the same as the away team.");
		}

		setStartDate(match.getStartDate());
		setResult(match.getResult());
		setRound(match.getRound());
		setYear(match.getYear());
		setWinner();
		setFinished();
		setPrediction(match.getPrediction());
		setCorrectlyPredicted();
		setLastModified();
		return match;
	}
}
