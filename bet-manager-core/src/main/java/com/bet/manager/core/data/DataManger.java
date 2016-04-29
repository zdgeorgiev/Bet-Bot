package com.bet.manager.core.data;

import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.core.data.sources.exceptions.InvalidMatchRoundIndex;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DataManger {

	/**
	 * Method which creates data for given home, away team, year and round. These method should be used
	 * while want to get information about future match, just passes the current year and expected round.
	 * The data is in the format :
	 * [round] (for both teams - [ladderPosition] [currentRoundStats] [venue] [prevRoundStats] [lastFiveGames])
	 * <p/>
	 * [ladderPosition] - Current position in the ranking table for the round.
	 * [currentRoundStats] - Current points and round goals difference.
	 * [venue] - Home/Away
	 * [prevRoundStats] - [track distance] [sprints] [passes] [shots] [fouls].
	 * [lastFiveGames] - Consist of 5 type of game end. Huge win [HW], Huge Lose [HL], Win [W], Lose [W], Tie [T],
	 * home team goals -  away team goals >= 2 - HW, <= 2 - HL, else W,L,T.
	 * <p/>
	 *
	 * @param homeTeam     homeTeam as Bundesliga name
	 * @param awayTeam     awayTeam as Bundesliga name
	 * @param year         match year
	 * @param round        match round should be at least 2nd one, because information for round 0 is invalid
	 * @param crawledPages hashMap which is responsible for the memorization, not to crawl already crawled pages
	 * @return Data for the match
	 */
	public static String getDataForMatch(String homeTeam, String awayTeam, int year, int round,
			Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		StringBuilder currentMatchData = new StringBuilder();
		String homeTeamData = getDataForTeam(homeTeam, year, round, crawledPages);
		String awayTeamData = getDataForTeam(awayTeam, year, round, crawledPages);

		currentMatchData
				.append(round)
				.append(" ")
				.append(homeTeamData)
				.append(" ")
				.append(awayTeamData);

		return currentMatchData.toString();
	}

	private static String getDataForTeam(String team, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		if (round <= 1)
			throw new InvalidMatchRoundIndex("Match index " + round + " cannot be less than 2nd one.");

		StringBuilder currentTeamData = new StringBuilder();

		currentTeamData
				.append(Bundesliga.getTeamRankingPlace(team, year, round, crawledPages))
				.append(" ")
				.append(Bundesliga.getCurrentRankingStats(team, year, round, crawledPages))
				.append(" ")
				.append(ResultDB.getTeamOpponentAndVenue(team, year, round, crawledPages)[1])
				.append(" ")
				.append(Bundesliga.getPrevRoundTeamPerformance(team, year, round, crawledPages))
				.append(" ")
				.append(ResultDB.getLastFiveGamesForTeam(team, year, round, crawledPages));

		return currentTeamData.toString();
	}
}