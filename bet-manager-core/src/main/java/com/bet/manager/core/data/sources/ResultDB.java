package com.bet.manager.core.data.sources;

import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

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

	private static final WebCrawler crawler = new WebCrawler();

	public static String parseResultsForPastFiveGames(String teamNameFromBundesliga, int year, int round)
			throws MalformedURLException, InterruptedException {
		return parseResultsForPastFiveGames(teamNameFromBundesliga, year, round, Collections.emptyMap());
	}

	public static String parseResultsForPastFiveGames(String teamNameFromBundesliga, int year, int round,
			Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(teamNameFromBundesliga);

		if (StringUtils.isBlank(resultDBTeamName)) {
			throw new IllegalStateException(
					"Cannot find any mapping to team '" + teamNameFromBundesliga + "' in TeamsMapping HashMap.");
		}

		URL allMatchesForTeamURL =
				new URL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = getContentOfPage(allMatchesForTeamURL, crawledPages);

		return getResultsForPastFiveGames(content, round);
	}

	private static String getContentOfPage(URL url, Map<URL, String> crawledPages) throws InterruptedException {
		if (crawledPages.containsKey(url)) {
			log.info("Returning cached copy of '{}'", url);
			return crawledPages.get(url);
		}

		// Put asleep the thread for 3-5 seconds
		Thread.sleep(new Random().nextInt(2000) + 3000);

		String contentOfPage = crawler.crawl(url);
		crawledPages.put(url, contentOfPage);
		return contentOfPage;
	}

	/**
	 * Collects data for last five matches for given team. If the matches before the
	 * given round is less than five will take the available ones.
	 *
	 * @param allMatchesHTML containing all the matches for given team
	 * @param round          to get the last matches before the round
	 * @return data format ({HugeWin} {HugeLoss} {Win} {Loss} {Tie}). WHen the huge outcome
	 * is made if the absolute value between the goals is greater than 1
	 */
	public static String getResultsForPastFiveGames(String allMatchesHTML, int round) {

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();

		int[] matchesNormalization = new int[5];
		int matchesToLook = Math.min(round - 1, 5);

		for (int i = 0; i < matchesToLook; i++) {
			Element currentMatch = e.children().get(0).children().get(matchesCount - round + i + 1);

			String result = currentMatch.children().get(3).text();
			String score = currentMatch.children().get(4).text();
			addMatchToNormalizationArray(result, score, matchesNormalization);
		}

		StringBuilder output = new StringBuilder();
		for (int match : matchesNormalization) {
			output.append(match).append(" ");
		}

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
		int differenceInScore =
				Math.abs(Integer.parseInt(rawNumbers[0].trim()) - Integer.parseInt(rawNumbers[1].trim()));

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

	public static String[] parseCurrentTeamOpponentAndVenue(String homeTeam, int year, int round)
			throws MalformedURLException, InterruptedException {
		return parseCurrentTeamOpponentAndVenue(homeTeam, year, round, Collections.emptyMap());
	}

	public static String[] parseCurrentTeamOpponentAndVenue(String homeTeam, int year, int round,
			Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(homeTeam);
		URL allMatchesForTeamURL =
				new URL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = getContentOfPage(allMatchesForTeamURL, crawledPages);

		return getCurrentTeamOpponentAndVenue(content, round);
	}

	/**
	 * Method parses the given html as string and return the target round match opponent and venue
	 * Venue is Away or Home. The Venue is -1 if the current team is Away and 1 if the current team is Home.
	 * The opponent is mapped to match the bundesliga name since was raw parsed from resultdb
	 *
	 * @param allMatchesHTML contains the arbitrary team all matches for arbitrary year
	 * @param round          to know which opponent should be returned
	 * @return the opponent and venue stored in array (0 => opponent, 1 => venue)
	 */
	public static String[] getCurrentTeamOpponentAndVenue(String allMatchesHTML, int round) {

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String mappedOpponent = TeamsMapping.resultDBToBundesliga.get(currentMatch.children().get(1).text());
		String venue = currentMatch.children().get(2).text();
		String venueNormalization = venue.equals(AWAY_TEAM_LITERAL) ? "-1" : "1";

		return new String[] { mappedOpponent, venueNormalization };
	}
}
