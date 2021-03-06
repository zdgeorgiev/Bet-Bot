package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.URLUtils;
import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.exceptions.MatchResultNotFound;
import com.bet.manager.model.entity.MatchVenueType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

@Deprecated
public final class ResultDB {

	private static final Logger LOG = LoggerFactory.getLogger(ResultDB.class);

	private static final String RESULTDB_DOMAIN = "http://www.resultdb.com/";
	private static final String RESULTDB_MATCHES_FOR_TEAM_URL = "germany/%s/%s/";

	private static final String TABLE_SELECTOR = "table.results";

	private static final String AWAY_TEAM_LITERAL = "Away";
	private static final String DRAW_GAME_LITERAL = "D";
	private static final String WIN_GAME_LITERAL = "W";
	private static final String LOSE_GAME_LITERAL = "L";
	private static final String RESULT_SPLITERATOR = "-";

	private static final String HUGE_WINS = "hugeWins";
	private static final String HUGE_LOSES = "hugeLoses";
	private static final String WINS = "wins";
	private static final String LOSES = "loses";
	private static final String DRAWS = "draws";

	private ResultDB() {
	}

	public static Map<String, Integer> getLastFiveGames(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		LOG.debug("Trying to parse the last five games for team '{}' year {} round {}..", bundesLigaTeam, year, round);
		return parseLastFiveGamesForTeam(content, round);
	}

	public static Map<String, Integer> parseLastFiveGamesForTeam(String allMatchesHTML, int round) {

		Map<String, Integer> lastFiveMatchesHistogram = new LinkedHashMap<>();
		lastFiveMatchesHistogram.put(HUGE_WINS, 0);
		lastFiveMatchesHistogram.put(HUGE_LOSES, 0);
		lastFiveMatchesHistogram.put(WINS, 0);
		lastFiveMatchesHistogram.put(LOSES, 0);
		lastFiveMatchesHistogram.put(DRAWS, 0);

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size() - 1;
		LOG.debug("Found {} matches total.", matchesCount);

		int matchesToLook = Math.min(round - 1, 5);
		LOG.debug("Getting last {} matches.", matchesToLook);

		for (int i = 0; i < matchesToLook; i++) {
			Element currentMatch = e.children().get(0).children().get(matchesCount - round + 2 + i);

			String result = currentMatch.children().get(3).text();
			String score = currentMatch.children().get(4).text();
			addToHistogram(result, score, lastFiveMatchesHistogram);
		}

		LOG.debug("Successfully get information about last {} matches", matchesToLook);
		return lastFiveMatchesHistogram;
	}

	public static void addToHistogram(String result, String score, Map<String, Integer> lastFiveMatchesHistogram) {

		String[] rawNumbers = score.split(RESULT_SPLITERATOR);
		int differenceInScore =
				Math.abs(Integer.parseInt(rawNumbers[0].trim()) - Integer.parseInt(rawNumbers[1].trim()));

		switch (result) {
		case DRAW_GAME_LITERAL:
			lastFiveMatchesHistogram.put(DRAWS, lastFiveMatchesHistogram.get(DRAWS) + 1);
			break;
		case WIN_GAME_LITERAL:
			if (differenceInScore > 1)
				lastFiveMatchesHistogram.put(HUGE_WINS, lastFiveMatchesHistogram.get(HUGE_WINS) + 1);
			else
				lastFiveMatchesHistogram.put(WINS, lastFiveMatchesHistogram.get(WINS) + 1);
			break;
		case LOSE_GAME_LITERAL:
			if (differenceInScore > 1)
				lastFiveMatchesHistogram.put(HUGE_LOSES, lastFiveMatchesHistogram.get(HUGE_LOSES) + 1);
			else
				lastFiveMatchesHistogram.put(LOSES, lastFiveMatchesHistogram.get(LOSES) + 1);
			break;
		default:
			throw new IllegalArgumentException("Invalid result state " + result);
		}
	}

	public static String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		LOG.debug("Getting opponent for team '{}' for match in year {} round {}", bundesLigaTeam, year, round);
		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		return parseTeamOpponent(content, round);
	}

	public static String parseTeamOpponent(String allMatchesHTML, int round) {
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String mappedOpponent = TeamsMapping.resultDBToBundesliga.get(currentMatch.children().get(1).text());
		LOG.debug("Opponent : '{}'", mappedOpponent);
		return mappedOpponent;
	}

	public static MatchVenueType getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		LOG.debug("Getting opponent for team '{}' for match in year {} round {}", bundesLigaTeam, year, round);
		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		return parseMatchVenue(content, round);
	}

	public static MatchVenueType parseMatchVenue(String allMatchesHTML, int round) {
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String venue = currentMatch.children().get(2).text();
		LOG.debug("Venue : {}", venue);
		return venue.equals(AWAY_TEAM_LITERAL) ? MatchVenueType.AWAY : MatchVenueType.HOME;
	}

	public static String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String resultDBTeam = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);
		URL teamMatchesURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeam, year));

		String allMatchesHTML = WebCrawler.crawl_ISO8858_9(teamMatchesURL, crawledPages);

		return parseMatchResult(round, allMatchesHTML);
	}

	public static String parseMatchResult(int round, String allMatchesHTML) {

		LOG.debug("Getting result for the match..");
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size() - 1;

		if (matchesCount - round < 0 || round < 1) {
			throw new MatchResultNotFound();
		}

		Element currentMatch = e.children().get(0).children().get(matchesCount - round + 1);

		String result = currentMatch.children().get(4).text();
		LOG.debug("Result found : {}", result);
		return result.trim();
	}
}
