package com.bet.manager.core.data.sources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class FootballDataUtils {

	private FootballDataUtils() {
	}

	/**
	 * Method which returns the position in the ranking table for team in given
	 * round and year. This method uses external memorization maps for already crawled
	 * pages.
	 *
	 * @param bundesLigaTeam team name
	 * @param year           match year
	 * @param round          match round
	 * @param crawledPages   memorization map for already crawled pages
	 * @return current round place
	 */
	public static int getTeamRankingPlace(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {
		return Bundesliga.getTeamRankingPlace(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Get points for given team in year and round
	 *
	 * @param bundesLigaTeam team name
	 * @return statistics for round
	 */
	public static int getPoints(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {
		return Bundesliga.getPoints(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Get goal difference for given team in year and round
	 *
	 * @param bundesLigaTeam team name
	 * @return statistics for round
	 */
	public static int getGoalDifference(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {
		return Bundesliga.getGoalDifference(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Method which is getting information about previous round by given round team performance
	 * (collects the data about total distance, sprints, passes, shots, fouls and if any of them
	 * is leaved empty the value from the average round stats will come in place)
	 * This method using external memorization maps for already crawled pages.
	 *
	 * @param bundesLigaTeam which wanna get performance
	 * @param year           year for match
	 * @param round          round for match ( the method will call with round - 1, to get the previous )
	 * @param crawledPages   memorization map for already crawled pages
	 * @return Map contains the statistics
	 */
	public static Map<String, Integer> getTeamPerformance(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {
		return Bundesliga.getTeamPerformance(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Method parses the given html as string and return the target round match venue (Away of Home).
	 *
	 * @param bundesLigaTeam team
	 * @param round          to know which match should look for
	 * @param year           year for the match
	 * @param crawledPages   memorization for already crawled pages
	 */
	public static String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages) throws Exception {
		return ResultDB.getMatchVenue(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 * Previous matches will be in range [round - 5, round)
	 *
	 * @param bundesLigaTeam team name
	 * @param year           year of the match
	 * @param round          round of the match
	 */
	public static Map<String, Integer> getLastFiveGames(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return ResultDB.getLastFiveGames(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Method which getting result for given team in specific round and year. This method uses external
	 * memorization for already crawled pages and crawlings.
	 *
	 * @param bundesLigaTeam team name
	 * @param year           year for match
	 * @param round          round for match
	 * @param crawledPages   memorization map
	 * @return the result in string format (X-Y) if this exist
	 */
	public static String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return ResultDB.getMatchResult(bundesLigaTeam, year, round, crawledPages);
	}

	/**
	 * Method parses the given html as string and return the target round match opponent.
	 * This method is using internal crawlings.
	 *
	 * @param bundesLigaTeam team name
	 * @param round          to know which match should look for
	 * @param year           year for the match
	 * @param crawledPages   memorization for already crawled pages
	 */
	public static String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return ResultDB.getTeamOpponent(bundesLigaTeam, year, round, crawledPages);
	}
}
