package com.bet.manager.core.data.sources;

import java.net.URL;
import java.util.Map;

public interface ISecondarySource {

	/**
	 * Method parses the given html as string and return the target round match opponent
	 *
	 * @param allMatchesHTML contains the arbitrary team all matches for arbitrary year
	 * @param round          to know which match should look for
	 */
	String parseTeamOpponent(String allMatchesHTML, int round);

	/**
	 * Method parses the given html as string and return the target round match opponent.
	 * This method is using internal crawlings.
	 *
	 * @param team         team
	 * @param round        to know which match should look for
	 * @param year         year for the match
	 * @param crawledPages memorization for already crawled pages
	 */
	String getTeamOpponent(String team, int year, int round, Map<URL, String> crawledPages)
			throws Exception;

	/**
	 * Get result for specific round match from html
	 *
	 * @param round          round for the match
	 * @param allMatchesHTML html containing all matches for arbitrary team
	 * @return the result in string format (X-Y) if this exist
	 */
	String parseMatchResult(int round, String allMatchesHTML);

	/**
	 * Method which getting result for given team in specific round and year. This method uses external
	 * memorization for already crawled pages and crawlings.
	 *
	 * @param team         team name
	 * @param year         year for match
	 * @param round        round for match
	 * @param crawledPages memorization map
	 * @return the result in string format (X-Y) if this exist
	 */
	String getMatchResult(String team, int year, int round, Map<URL, String> crawledPages) throws Exception;

	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 *
	 * @param allMatchesHTML containing all the matches for given team
	 * @param round          to get the last matches before the round
	 */
	String parseLastFiveGamesForTeam(String allMatchesHTML, int round);

	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 *
	 * @param team  team name
	 * @param year  year of the match
	 * @param round round of the match
	 */
	String getLastFiveGamesForTeam(String team, int year, int round, Map<URL, String> crawledPages)
			throws Exception;

	/**
	 * Method parses the given html as string and return the target round match venue (Away of Home).
	 *
	 * @param team         team
	 * @param round        to know which match should look for
	 * @param year         year for the match
	 * @param crawledPages memorization for already crawled pages
	 */
	String getMatchVenue(String team, int year, int round, Map<URL, String> crawledPages) throws Exception;

	/**
	 * Method parses the given html as string and return the target round match venue (Away of Home)
	 *
	 * @param allMatchesHTML contains the arbitrary team all matches for arbitrary year
	 * @param round          to know which match should look for
	 * @return The Venue is -1 if the current team is Away and 1 if the current team is Home.
	 */
	String parseMatchVenue(String allMatchesHTML, int round);

	/**
	 * Function which update the matchesNormalization param with the given score depends
	 * of the result. For huge outcome is accepted if the score difference is bigger than 1
	 * and the result can be one of the following {W,L,D}
	 *
	 * @param result               W,L,D
	 * @param score                e.g (4-2)
	 * @param matchesNormalization contains array with 4 options {HugeWin, HugeLoss, Win, Loss, Tie}
	 */
	void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization);
}
