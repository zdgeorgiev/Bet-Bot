package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.URLUtils;
import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.data.sources.exceptions.InvalidMappingException;
import com.bet.manager.core.data.sources.exceptions.MatchResultNotFound;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class ResultDB {

	private static final Logger log = LoggerFactory.getLogger(ResultDB.class);

	private static final String RESULTDB_DOMAIN = "http://www.resultdb.com/";
	private static final String RESULTDB_MATCHES_FOR_TEAM_URL = "germany/%s/%s/";

	private static final String TABLE_SELECTOR = "table.results";

	private static final String AWAY_TEAM_LITERAL = "Away";
	private static final String DRAW_GAME_LITERAL = "D";
	private static final String WIN_GAME_LITERAL = "W";
	private static final String LOSE_GAME_LITERAL = "L";
	private static final String RESULT_SPLITERATOR = "-";

	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 *
	 * @param team  team name
	 * @param year  year of the match
	 * @param round round of the match
	 */
	public static String getLastFiveGamesForTeam(String team, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(team);

		if (StringUtils.isBlank(resultDBTeamName)) {
			throw new InvalidMappingException("Cannot find any mapping to team '" + team + "' in bundesliga to resultdb HashMap.");
		}

		URL allMatchesForTeamURL =
				URLUtils.createSafeURL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl(allMatchesForTeamURL, crawledPages);

		log.debug("Trying to parse the last five games for team '{}' year {} round {}..", team, year, round);
		return parseLastFiveGamesForTeam(content, round);
	}

	/**
	 * Collects data for last five matches for given team depends on the given round.
	 * If the matches before the given round is less than five will take the available ones.
	 *
	 * @param allMatchesHTML containing all the matches for given team
	 * @param round          to get the last matches before the round
	 * @return data format ({HugeWin} {HugeLoss} {Win} {Loss} {Tie}). WHen the huge outcome
	 * is made if the absolute value between the goals is greater than 1
	 */
	public static String parseLastFiveGamesForTeam(String allMatchesHTML, int round) {

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size() - 1;
		log.debug("Found {} matches total.", matchesCount);

		int[] matchesNormalization = new int[5];
		int matchesToLook = Math.min(round - 1, 5);
		log.debug("Getting last {} matches.", matchesToLook);

		for (int i = 0; i < matchesToLook; i++) {
			Element currentMatch = e.children().get(0).children().get(matchesCount - round + 2 + i);

			String result = currentMatch.children().get(3).text();
			String score = currentMatch.children().get(4).text();
			addMatchToNormalizationArray(result, score, matchesNormalization);
		}

		StringBuilder output = new StringBuilder();
		for (int match : matchesNormalization) {
			output.append(match).append(" ");
		}

		log.debug("Successfully get information about last {} matches", matchesToLook);
		return output.toString().trim();
	}

	/**
	 * Function which update the matchesNormalization param with the given score depends
	 * of the result. For huge outcome is accepted if the score difference is bigger than 1
	 * and the result can be one of the following {W,L,D}
	 *
	 * @param result               W,L,D
	 * @param score                e.g (4-2)
	 * @param matchesNormalization contains array with 4 options {HugeWin, HugeLoss, Win, Loss, Tie}
	 */
	public static void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization) {

		String[] rawNumbers = score.split(RESULT_SPLITERATOR);
		int differenceInScore = Math.abs(Integer.parseInt(rawNumbers[0].trim()) - Integer.parseInt(rawNumbers[1].trim()));

		switch (result) {
		case DRAW_GAME_LITERAL:
			matchesNormalization[4]++;
			break;
		case WIN_GAME_LITERAL:
			if (differenceInScore > 1) {
				matchesNormalization[0]++;
			} else {
				matchesNormalization[2]++;
			}
			break;
		case LOSE_GAME_LITERAL:
			if (differenceInScore > 1) {
				matchesNormalization[1]++;
			} else {
				matchesNormalization[3]++;
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid result state " + result);
		}
	}

	/**
	 * Method which find a opponent for given team and also find the venue for the match.
	 * This method is using internally crawling to the required pages.
	 *
	 * @param team         team name
	 * @param year         year of the match
	 * @param round        round of the match
	 * @param crawledPages memorization map for already crawled pages
	 * @return the opponent and venue stored in array (0 => opponent, 1 => venue)
	 */
	public static String[] getTeamOpponentAndVenue(String team, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		log.debug("Getting opponent for team '{}' and end result for match in year {} round {}", team, year, round);
		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(team);
		URL allMatchesForTeamURL =
				URLUtils.createSafeURL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl(allMatchesForTeamURL, crawledPages);

		return parseTeamOpponentAndVenue(content, round);
	}

	/**
	 * Method parses the given html as string and return the target round match opponent and venue
	 * Venue is Away or Home. The Venue is -1 if the current team is Away and 1 if the current team is Home.
	 * The opponent is mapped to match the bundesliga name since was raw parsed from resultdb.
	 *
	 * @param allMatchesHTML contains the arbitrary team all matches for arbitrary year
	 * @param round          to know which opponent should be returned
	 * @return the opponent and venue stored in array (0 => opponent, 1 => venue)
	 */
	public static String[] parseTeamOpponentAndVenue(String allMatchesHTML, int round) {

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String mappedOpponent = TeamsMapping.resultDBToBundesliga.get(currentMatch.children().get(1).text());
		log.debug("Opponent : '{}'", mappedOpponent);
		String venue = currentMatch.children().get(2).text();
		log.debug("Venue : {}", venue);
		String venueNormalization = venue.equals(AWAY_TEAM_LITERAL) ? "-1" : "1";

		return new String[] { mappedOpponent, venueNormalization };
	}

	/**
	 * Method which getting result for given team in specific round and year. This method uses external
	 * memorization for already crawled pages and crawlings.
	 *
	 * @param team         team name
	 * @param year         year for match
	 * @param round        round for match
	 * @param crawledPages memorization map
	 * @return the result in string format (X-Y) if this exist
	 * @throws MatchResultNotFound if the result is not found.
	 */
	public static String getMatchResult(String team, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		URL teamMatchesURL = URLUtils.createSafeURL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, year, team));

		String allMatchesHTML = WebCrawler.crawl(teamMatchesURL, crawledPages);

		String result = "-1";

		try {
			result = parseMatchResult(round, allMatchesHTML);
		} catch (MatchResultNotFound e) {
			log.error("Result for team {} in round {} year {} is not found. Maybe the match is not started or over yet.",
					team, round, year);
		}

		return result;
	}

	/**
	 * Get result for specific round match from html
	 *
	 * @param round          round for the match
	 * @param allMatchesHTML html containing all matches for arbitrary team
	 * @return the result in string format (X-Y) if this exist
	 * @throws MatchResultNotFound if the result is not found.
	 */
	public static String parseMatchResult(int round, String allMatchesHTML) {

		log.debug("Getting result for the match..");
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size() - 1;

		if (matchesCount - round < 0 || round < 1) {
			throw new MatchResultNotFound();
		}

		Element currentMatch = e.children().get(0).children().get(matchesCount - round + 1);

		String score = currentMatch.children().get(4).text();
		log.debug("Result found : {}", score);

		return score.trim();
	}
}
