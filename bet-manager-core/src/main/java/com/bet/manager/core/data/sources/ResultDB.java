package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.URLUtils;
import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.exceptions.InvalidMappingException;
import com.bet.manager.core.exceptions.MatchResultNotFound;
import com.bet.manager.model.dao.MatchMetaData;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;

public class ResultDB implements ISecondarySource {

	private static final Logger log = LoggerFactory.getLogger(ResultDB.class);

	private static final String RESULTDB_DOMAIN = "http://www.resultdb.com/";
	private static final String RESULTDB_MATCHES_FOR_TEAM_URL = "germany/%s/%s/";

	private static final String DELIMITER = MatchMetaData.CONSTRUCTOR_PARAMS_DELIMITER;

	private static final String TABLE_SELECTOR = "table.results";

	private static final String AWAY_TEAM_LITERAL = "Away";
	private static final String DRAW_GAME_LITERAL = "D";
	private static final String WIN_GAME_LITERAL = "W";
	private static final String LOSE_GAME_LITERAL = "L";
	private static final String RESULT_SPLITERATOR = "-";

	public ResultDB() {
	}

	@Override
	public String getLastFiveGamesForTeam(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		if (StringUtils.isBlank(resultDBTeamName)) {
			throw new InvalidMappingException(
					"Cannot find any mapping to team '" + bundesLigaTeam + "' in bundesliga to resultdb HashMap.");
		}

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl(allMatchesForTeamURL, crawledPages);

		log.debug("Trying to parse the last five games for team '{}' year {} round {}..", bundesLigaTeam, year, round);
		return parseLastFiveGamesForTeam(content, round);
	}

	@Override
	public String parseLastFiveGamesForTeam(String allMatchesHTML, int round) {

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
			output.append(match).append(DELIMITER);
		}

		log.debug("Successfully get information about last {} matches", matchesToLook);
		return output.substring(0, output.length() - 1);
	}

	@Override
	public String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		log.debug("Getting opponent for team '{}' for match in year {} round {}", bundesLigaTeam, year, round);
		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl(allMatchesForTeamURL, crawledPages);

		return parseTeamOpponent(content, round);
	}

	@Override
	public String parseTeamOpponent(String allMatchesHTML, int round) {
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String mappedOpponent = TeamsMapping.resultDBToBundesliga.get(currentMatch.children().get(1).text());
		log.debug("Opponent : '{}'", mappedOpponent);
		return mappedOpponent;
	}

	@Override
	public String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		log.debug("Getting opponent for team '{}' for match in year {} round {}", bundesLigaTeam, year, round);
		String resultDBTeamName = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = WebCrawler.crawl(allMatchesForTeamURL, crawledPages);

		return parseMatchVenue(content, round);
	}

	@Override
	public String parseMatchVenue(String allMatchesHTML, int round) {
		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element currentMatch = e.children().get(0).children().get(matchesCount - round);

		String venue = currentMatch.children().get(2).text();
		log.debug("Venue : {}", venue);
		return venue.equals(AWAY_TEAM_LITERAL) ? "-1" : "1";
	}

	@Override
	public void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization) {

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

	@Override
	public String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String resultDBTeam = TeamsMapping.bundesligaToResultDB.get(bundesLigaTeam);
		URL teamMatchesURL = URLUtils.createSafeURL(
				String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeam, year));

		String allMatchesHTML = WebCrawler.crawl(teamMatchesURL, crawledPages);

		String result = null;

		try {
			result = parseMatchResult(round, allMatchesHTML);
		} catch (Exception e) {
			log.error("Result for team {} in round {} year {} is not found."
					+ " Maybe the match is not started or over yet.", bundesLigaTeam, round, year);
		}

		return result;
	}

	@Override
	public String parseMatchResult(int round, String allMatchesHTML) {

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
