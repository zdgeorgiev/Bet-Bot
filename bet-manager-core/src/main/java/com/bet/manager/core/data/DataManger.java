package com.bet.manager.core.data;

import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ResultDB;
import org.w3c.dom.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class DataManger {

	/**
	 * Method which creates data for given home, away team, year and round. These method should be used
	 * while want to get information about future match, just passes the current year and expected round.
	 * The data is in the format :
	 * [round] (for both teams - [ladderPosition] [currentRoundStats] [venue] [prevRoundStats] [lastFiveGames])
	 * <p>
	 * [ladderPosition] - Current position in the ranking table for the round.
	 * [currentRoundStats] - Current points and round goals difference.
	 * [venue] - Home/Away
	 * [prevRoundStats] - [track distance] [sprints] [passes] [shots] [fouls].
	 * [lastFiveGames] - Consist of 5 type of game end. Huge win [HW], Huge Lose [HL], Win [W], Lose [W], Tie [T],
	 * home team goals -  away team goals >= 2 - HW, <= 2 - HL, else W,L,T.
	 * <p>
	 *
	 * @param homeTeam homeTeam in Bundesliga name format
	 * @param awayTeam awayTeam in Bundesliga name format
	 * @param year     match year
	 * @param round    match round
	 * @return Data for the match
	 */
	public static String getDataForMatch(String homeTeam, String awayTeam, int year, int round)
			throws MalformedURLException, InterruptedException {
		return getDataForMatch(homeTeam, awayTeam, year, round, Collections.emptyMap(), Collections.emptyMap());
	}

	/**
	 * Method which creates data for given home, away team, year and round. These method should be used
	 * while want to get information about future match, just passes the current year and expected round.
	 * The data is in the format :
	 * [round] (for both teams - [ladderPosition] [currentRoundStats] [venue] [prevRoundStats] [lastFiveGames])
	 * <p>
	 * [ladderPosition] - Current position in the ranking table for the round.
	 * [currentRoundStats] - Current points and round goals difference.
	 * [venue] - Home/Away
	 * [prevRoundStats] - [track distance] [sprints] [passes] [shots] [fouls].
	 * [lastFiveGames] - Consist of 5 type of game end. Huge win [HW], Huge Lose [HL], Win [W], Lose [W], Tie [T],
	 * home team goals -  away team goals >= 2 - HW, <= 2 - HL, else W,L,T.
	 * <p>
	 * * This method uses a memorization maps for performance boost, but its highly recommended to use its overload.
	 *
	 * @param homeTeam homeTeam as Bundesliga name
	 * @param awayTeam awayTeam as Bundesliga name
	 * @param year     match year
	 * @param round    match round
	 * @return Data for the match
	 */
	public static String getDataForMatch(String homeTeam, String awayTeam, int year, int round,
			Map<URL, String> crawledPages, Map<String, Document> parsedDocuments)
			throws MalformedURLException, InterruptedException {

		StringBuilder currentMatchData = new StringBuilder();
		String homeTeamData = getDataForTeam(homeTeam, year, round, crawledPages, parsedDocuments);
		String awayTeamData = getDataForTeam(awayTeam, year, round, crawledPages, parsedDocuments);

		currentMatchData
				.append(round)
				.append(" ")
				.append(homeTeamData)
				.append(" ")
				.append(awayTeamData);

		return currentMatchData.toString();
	}

	private static String getDataForTeam(String team, int year, int round, Map<URL, String> crawledPages,
			Map<String, Document> parsedDocuments)
			throws MalformedURLException, InterruptedException {

		StringBuilder currentTeamData = new StringBuilder();

		currentTeamData
				.append(Bundesliga.getTeamRankingPlace(team, year, round, crawledPages, parsedDocuments))
				.append(" ")
				.append(Bundesliga.getCurrentRankingStats(team, year, round, crawledPages, parsedDocuments))
				.append(" ")
				.append(ResultDB.getTeamOpponentAndVenue(team, year, round, crawledPages)[1])
				.append(" ")
				.append(Bundesliga.getPrevRoundTeamPerformance(team, year, round, crawledPages, parsedDocuments))
				.append(" ")
				.append(ResultDB.getLastFiveGamesForTeam(team, year, round, crawledPages));

		return currentTeamData.toString();
	}
}