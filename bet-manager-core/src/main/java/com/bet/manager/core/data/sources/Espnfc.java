package com.bet.manager.core.data.sources;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.commons.util.URLUtils;
import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.model.entity.MatchVenueType;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Espnfc {

	private static final Logger LOG = LoggerFactory.getLogger(Espnfc.class);

	private static final String ESPNFC_DOMAIN = "http://www.espnfc.us/club/%s";
	private static final String ESPNFC_MATCHES_FOR_TEAM_URL = "/fixtures?leagueId=10&season=%s&xhr=1";

	private static final String HUGE_WINS = "hugeWins";
	private static final String HUGE_LOSES = "hugeLoses";
	private static final String WINS = "wins";
	private static final String LOSES = "loses";
	private static final String DRAWS = "draws";

	private Espnfc() {
	}

	public static Map<String, Integer> getLastFiveGames(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String espnfcTeamName = TeamsMapping.bundesligaToESPNFC.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(ESPNFC_DOMAIN + ESPNFC_MATCHES_FOR_TEAM_URL, TeamsMapping.ESPNFCToURI.get(espnfcTeamName), year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		LOG.debug("Trying to parse the last five games for team '{}' year {} round {}..", bundesLigaTeam, year, round);

		String htmlContent = ((JSONObject) (new JSONObject(content).get("content"))).get("html").toString();

		return parseLastFiveGamesForTeam(htmlContent, espnfcTeamName, year, round);
	}

	public static Map<String, Integer> parseLastFiveGamesForTeam(String allMatchesHTML, String espnfcTeamName, int year,
			int round) {

		Map<String, Integer> lastFiveMatchesHistogram = new LinkedHashMap<>();
		lastFiveMatchesHistogram.put(HUGE_WINS, 0);
		lastFiveMatchesHistogram.put(HUGE_LOSES, 0);
		lastFiveMatchesHistogram.put(WINS, 0);
		lastFiveMatchesHistogram.put(LOSES, 0);
		lastFiveMatchesHistogram.put(DRAWS, 0);

		Elements matches = Jsoup.parse(allMatchesHTML)
				.getElementsByClass("games-container").get(1).getElementsByClass("score-list");

		int matchesToLook = Math.min(round - 1, 5);
		LOG.debug("Getting last {} matches.", matchesToLook);

		int firstMatchIndex = Math.max(0, round - 1 - 5);

		for (int i = 0; i < matchesToLook; i++) {
			Element match = matches.get(firstMatchIndex + i);
			Assert.assertEquals("Trying to get result for not finished match", "score-list complete", match.attr("class"));

			Element matchStats = match.getElementsByClass("score-result").get(0).getElementsByClass("result").get(0);
			Integer homeTeamGoals = Integer.parseInt(matchStats.getElementsByClass("home-score").text());
			Integer awayTeamGoals = Integer.parseInt(matchStats.getElementsByClass("away-score").text());
			boolean isWinner = isWinner(match.getElementsByClass("team-name"), espnfcTeamName);

			addToHistogram(isWinner, homeTeamGoals, awayTeamGoals, lastFiveMatchesHistogram);
		}

		LOG.debug("Successfully get information about last {} matches", matchesToLook);
		return lastFiveMatchesHistogram;
	}

	private static boolean isWinner(Elements elements, String espnfcTeamName) {

		String homeTeamName = elements.get(0).parent().getElementsByClass("team-logo").get(0).child(0).attr("alt");

		return homeTeamName.toLowerCase().equals(espnfcTeamName.toLowerCase()) ?
				elements.get(0).attr("class").equals("team-name winner") :
				elements.get(1).attr("class").equals("team-name winner");
	}

	private static void addToHistogram(boolean winner, Integer homeTeamGoals, Integer awayTeamGoals,
			Map<String, Integer> lastFiveMatchesHistogram) {

		int differenceInScore = Math.abs(homeTeamGoals - awayTeamGoals);

		if (winner) {
			if (differenceInScore > 1)
				lastFiveMatchesHistogram.put(HUGE_WINS, lastFiveMatchesHistogram.get(HUGE_WINS) + 1);
			else
				lastFiveMatchesHistogram.put(WINS, lastFiveMatchesHistogram.get(WINS) + 1);
		} else {
			if (differenceInScore == 0)
				lastFiveMatchesHistogram.put(DRAWS, lastFiveMatchesHistogram.get(DRAWS) + 1);
			else if (differenceInScore > 1)
				lastFiveMatchesHistogram.put(HUGE_LOSES, lastFiveMatchesHistogram.get(HUGE_LOSES) + 1);
			else
				lastFiveMatchesHistogram.put(LOSES, lastFiveMatchesHistogram.get(LOSES) + 1);
		}
	}

	public static String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String espnfcTeamName = TeamsMapping.bundesligaToESPNFC.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(ESPNFC_DOMAIN + ESPNFC_MATCHES_FOR_TEAM_URL, TeamsMapping.ESPNFCToURI.get(espnfcTeamName), year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		LOG.debug("Getting opponent for team '{}' for match in year {} round {}", bundesLigaTeam, year, round);

		String htmlContent = ((JSONObject) (new JSONObject(content).get("content"))).get("html").toString();

		return parseTeamOpponent(htmlContent, espnfcTeamName, round);
	}

	public static String parseTeamOpponent(String allMatchesHTML, String espnfcTeamName, int round) {

		Elements matches = Jsoup.parse(allMatchesHTML)
				.getElementsByClass("games-container").get(1).getElementsByClass("score-list");

		Element match = matches.get(round - 1);

		String homeTeamName = match.getElementsByClass("team-name").get(0).parent().getElementsByClass("team-logo")
				.get(0).child(0).attr("alt");
		String awayTeamName = match.getElementsByClass("team-name").get(1).parent().getElementsByClass("team-logo")
				.get(0).child(0).attr("alt");

		String mappedOpponent = homeTeamName.toLowerCase().equals(espnfcTeamName.toLowerCase()) ?
				TeamsMapping.ESPNFCToBundesliga.get(awayTeamName) :
				TeamsMapping.ESPNFCToBundesliga.get(homeTeamName);
		LOG.debug("Opponent : '{}'", mappedOpponent);
		return mappedOpponent;
	}

	public static MatchVenueType getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String espnfcTeamName = TeamsMapping.bundesligaToESPNFC.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(ESPNFC_DOMAIN + ESPNFC_MATCHES_FOR_TEAM_URL, TeamsMapping.ESPNFCToURI.get(espnfcTeamName), year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		String htmlContent = ((JSONObject) (new JSONObject(content).get("content"))).get("html").toString();

		return parseMatchVenue(htmlContent, espnfcTeamName, round);
	}

	public static MatchVenueType parseMatchVenue(String allMatchesHTML, String espnfcTeamName, int round) {

		LOG.debug("Getting result for the match..");
		Element match = Jsoup.parse(allMatchesHTML)
				.getElementsByClass("games-container").get(1).getElementsByClass("score-list")
				.get(round - 1);

		String homeTeamName = match.getElementsByClass("team-name").get(0).parent().getElementsByClass("team-logo")
				.get(0).child(0).attr("alt");

		return homeTeamName.toLowerCase().equals(espnfcTeamName.toLowerCase()) ?
				MatchVenueType.HOME :
				MatchVenueType.AWAY;
	}

	public static String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {

		String espnfcTeamName = TeamsMapping.bundesligaToESPNFC.get(bundesLigaTeam);

		URL allMatchesForTeamURL = URLUtils.createSafeURL(
				String.format(ESPNFC_DOMAIN + ESPNFC_MATCHES_FOR_TEAM_URL, TeamsMapping.ESPNFCToURI.get(espnfcTeamName), year));

		String content = WebCrawler.crawl_ISO8858_9(allMatchesForTeamURL, crawledPages);

		String htmlContent = ((JSONObject) (new JSONObject(content).get("content"))).get("html").toString();

		return parseMatchResult(round, htmlContent);
	}

	public static String parseMatchResult(int round, String allMatchesHTML) {

		Elements matches = Jsoup.parse(allMatchesHTML)
				.getElementsByClass("games-container").get(1).getElementsByClass("score-list");

		Element match = matches.get(round - 1);

		Element matchStats = match.getElementsByClass("score-result").get(0).getElementsByClass("result").get(0);

		try {
			Integer homeTeamGoals = Integer.parseInt(matchStats.getElementsByClass("home-score").text());
			Integer awayTeamGoals = Integer.parseInt(matchStats.getElementsByClass("away-score").text());

			String result = homeTeamGoals + "-" + awayTeamGoals;
			LOG.debug("Result found : {}", result);
			return result;

		} catch (NumberFormatException e) {
			LOG.debug("Cannot create result for the match.. probably not finished.");
			return ResultMessages.UNKNOWN_RESULT;
		}
	}
}
