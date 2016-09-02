package com.bet.manager.core.data.sources;

import java.net.URL;
import java.util.Map;

public interface ISecondarySource {

	/**
	 * Method parses the given html as string and return the target round match opponent.
	 * This method is using internal crawlings.
	 *
	 * @param bundesLigaTeam team name
	 * @param round          to know which match should look for
	 * @param year           year for the match
	 * @param crawledPages   memorization for already crawled pages
	 */
	String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception;

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
	String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages) throws Exception;


	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 *
	 * @param bundesLigaTeam team name
	 * @param year           year of the match
	 * @param round          round of the match
	 */
	String getLastFiveGamesForTeam(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception;

	/**
	 * Method parses the given html as string and return the target round match venue (Away of Home).
	 *
	 * @param bundesLigaTeam team
	 * @param round          to know which match should look for
	 * @param year           year for the match
	 * @param crawledPages   memorization for already crawled pages
	 */
	String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages) throws Exception;
}
