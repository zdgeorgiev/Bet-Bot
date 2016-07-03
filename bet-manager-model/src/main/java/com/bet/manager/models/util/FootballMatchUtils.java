package com.bet.manager.models.util;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.FootballMatchWithPrediction;
import org.apache.commons.lang.StringUtils;

public class FootballMatchUtils {

	public static boolean equals(FootballMatchWithPrediction firstMatch, FootballMatchWithPrediction secondMatch) {

		return firstMatch.getHomeTeam().equals(secondMatch.getHomeTeam()) &&
				firstMatch.getAwayTeam().equals(secondMatch.getAwayTeam()) &&
				firstMatch.getStartDate().equals(secondMatch.getStartDate()) &&
				equalsWithNull(firstMatch.getResult(), secondMatch.getResult()) &&
				equalsWithNull(firstMatch.getWinner(), secondMatch.getWinner());
	}

	private static boolean equalsWithNull(String firstProp, String secondProp) {
		return firstProp == null ? secondProp == null : firstProp.equals(secondProp);
	}

	public static void setResultAndWinner(FootballMatch match, String result) {

		if (StringUtils.isBlank(result)) {
			match.setResult(ResultMessages.UNKNOWN_SCORE);
		} else {
			match.setResult(result.replace(" ", ""));
		}

		if (!match.getResult().contains(match.getResultDelimiter())) {
			throw new IllegalArgumentException(
					"Result " + result + " not contains the delimiter " + match.getResultDelimiter());
		}

		String winner;

		if (match.getResult().equals(ResultMessages.UNKNOWN_SCORE)) {
			winner = ResultMessages.NO_WINNER;
		} else {
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
		}

		match.setWinner(winner);
	}
}
