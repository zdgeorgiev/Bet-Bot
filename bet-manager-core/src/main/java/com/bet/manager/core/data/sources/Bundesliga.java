package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.DocumentUtils;
import com.bet.manager.commons.util.URLUtils;
import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.exceptions.IllegalTeamMappingException;
import com.bet.manager.core.exceptions.InvalidMappingException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bundesliga {

	private static final Logger log = LoggerFactory.getLogger(Bundesliga.class);

	private static final String BUNDESLIGA_DOMAIN = "http://www.bundesliga.com/";
	private static final String ROUND_MATCHES_URL =
			"data/feed/51/%s/post_standing/post_standing_%s.xml?cb=517837";
	private static final String TEAM_STATS_URL =
			"data/feed/51/%s/team_stats_round/team_stats_round_%s.xml?cb=544329";

	private static final String TRACK_DIST_ATTR = "imp:tracking-distance";
	private static final String TRACK_SPRINTS_ATTR = "imp:tracking-sprints";
	private static final String TRACK_PASSES_ATTR = "imp:passes-total";
	private static final String TRACK_SHOTS_ATTR = "shots-total";
	private static final String TRACK_FOULS_ATTR = "fouls-committed";

	private static final String TEAM_ATTR = "team";
	private static final String TEAM_KEY_ATTR = "team-key";
	private static final String CODE_NAME_ATTR = "code-name";
	private static final String CODE_KEY_ATTR = "code-key";
	private static final String CODE_TYPE_ATTR = "code-type";
	private static final String TEAM_ID_SPLITERATOR = "soccer.t_";
	private static final String SPORTS_CONTENT_ATTR = "sports-content-code";
	private static final String GROUP_STATS_ATTR = "group-stats";

	private Bundesliga() {
	}

	public static int getTeamRankingPlace(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		log.debug("Getting information about '{}' rank in round {} year {}", bundesLigaTeam, round, year);
		String currentRoundMatchesContent = Bundesliga.getMatches(year, round, crawledPages);

		Document currentRoundMatchesXML = DocumentUtils.parse(currentRoundMatchesContent);

		log.debug("Creating ranking table for round {} year {}", round, year);
		Map<String, Integer> currentRoundRanking = Bundesliga.createRankingTable(currentRoundMatchesXML);

		Integer teamId = currentRoundRanking.get(bundesLigaTeam);

		if (teamId == null)
			throw new InvalidMappingException(
					"There is not entry for team with name '" + bundesLigaTeam + "' in ranking table for year " + year);

		return teamId;
	}

	/**
	 * Getting all matches for given round and year.
	 *
	 * @param year         year of the matches
	 * @param round        round of the matches
	 * @param crawledPages memorization map for already crawled pages
	 * @return content page with all matches for the round in html format
	 */
	public static String getMatches(int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		URL prevRoundMatchesURL =
				URLUtils.createSafeURL(String.format(BUNDESLIGA_DOMAIN + ROUND_MATCHES_URL, year, round));

		return WebCrawler.crawl(prevRoundMatchesURL, crawledPages);
	}

	/**
	 * Create ranking table from the document
	 *
	 * @param doc parsed as xml
	 * @return Map containing pairs {team} => {rank}
	 */
	public static Map<String, Integer> createRankingTable(Document doc) {

		Map<String, Integer> ranking = new HashMap<>();
		NodeList teamNodes = doc.getElementsByTagName(SPORTS_CONTENT_ATTR);
		log.debug("Found {} teams in the rank table", teamNodes.getLength());

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			NamedNodeMap attributes = currentTeam.getAttributes();

			if (attributes.getNamedItem(CODE_TYPE_ATTR).getNodeValue().equals(TEAM_ATTR)) {

				int teamId = Integer.parseInt(
						attributes.getNamedItem(CODE_KEY_ATTR).getNodeValue().split(TEAM_ID_SPLITERATOR)[1]);
				String teamName = attributes.getNamedItem(CODE_NAME_ATTR).getNodeValue();

				log.debug("Team {}. -> '{}'", ranking.size() + 1, teamName);
				checkForValidMappingBundesligaIdToTeam(teamId, teamName);

				ranking.put(teamName, ranking.size() + 1);
			}
		}

		return ranking;
	}

	private static boolean checkForValidMappingBundesligaIdToTeam(int id, String bundesLigaTeam) {
		log.debug("Searching for valid mapping from BundesligaID => BundesligaName");

		if (TeamsMapping.bundesligaIdToName.get(id).equals(bundesLigaTeam))
			return true;

		throw new IllegalTeamMappingException("Team with id " + id + " is mapped to " + bundesLigaTeam +
				", but in the xml team node id " + id + " is mapped to " + TeamsMapping.bundesligaIdToName.get(id));
	}

	public static int getPoints(String bundesLigaTeam, int year, int round,
			Map<URL, String> crawledPages) throws MalformedURLException, InterruptedException {

		NodeList teamNodes = getTeamNodes(year, round, crawledPages);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			String currentTeamName = getNameFromTeamNode(currentTeam);

			if (currentTeamName.equals(bundesLigaTeam))
				return parsePoints(currentTeam);
		}

		throw new IllegalStateException(
				"Cannot get the current points for " + bundesLigaTeam + " year " + year + " round " + round);
	}

	private static NodeList getTeamNodes(int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		String currentRoundMatchesContent = Bundesliga.getMatches(year, round, crawledPages);
		Document currentRoundMatches = DocumentUtils.parse(currentRoundMatchesContent);

		return currentRoundMatches.getElementsByTagName(TEAM_ATTR);
	}

	public static int parsePoints(Node currentTeam) {

		NamedNodeMap attributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getAttributes();

		int points = Integer.parseInt(attributes.getNamedItem("standing-points").getNodeValue());
		log.debug("Points : {}", points);

		return points;
	}

	private static String getNameFromTeamNode(Node team) {
		return team.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getAttributes()
				.getNamedItem("full").getNodeValue();
	}

	public static int getGoalDifference(String bundesLigaTeam, int year, int round,
			Map<URL, String> crawledPages) throws MalformedURLException, InterruptedException {

		NodeList teamNodes = getTeamNodes(year, round, crawledPages);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			String currentTeamName = getNameFromTeamNode(currentTeam);

			if (currentTeamName.equals(bundesLigaTeam)) {
				return parseGoalDifference(currentTeam);
			}
		}

		throw new IllegalStateException(
				"Cannot get the current goal difference for " + bundesLigaTeam + " year " + year + " round " + round);
	}

	public static int parseGoalDifference(Node currentTeam) {

		NamedNodeMap attributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getAttributes();

		Integer homeGoals = Integer.parseInt(attributes.getNamedItem("points-scored-for").getNodeValue());
		Integer awayGoals = Integer.parseInt(attributes.getNamedItem("points-scored-against").getNodeValue());

		int goalDifference = homeGoals - awayGoals;
		log.debug("Goal difference : {}", goalDifference);

		return goalDifference;
	}

	public static Map<String, Integer> getTeamPerformance(String bundesLigaTeam, int year, int round,
			Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		log.debug("Getting information for team '{}' in round {} year {}", bundesLigaTeam, round, year);
		Map<String, Integer> prevRoundAverageStats =
				Bundesliga.getAverageRoundStats(year, round, crawledPages);

		URL prevRoundStatsURL =
				URLUtils.createSafeURL(String.format(BUNDESLIGA_DOMAIN + TEAM_STATS_URL, year, round));

		String prevRoundTeamStatsXML = WebCrawler.crawl(prevRoundStatsURL, crawledPages);

		log.debug("Parsing the information collect in the previous round", bundesLigaTeam, round, year);
		Map<String, Integer> teamPerformance = parseTeamPerformance(prevRoundTeamStatsXML, bundesLigaTeam, prevRoundAverageStats);

		if (teamPerformance.isEmpty())
			throw new IllegalStateException(
					"Failed to retrieve team performance for " + bundesLigaTeam + " year " + year + " round " + round);

		return teamPerformance;
	}

	public static Map<String, Integer> parseTeamPerformance(String prevRoundTeamStatsXML, String bundesLigaTeam,
			Map<String, Integer> prevRoundAverageStats) {

		Map<String, Integer> teamPerformance = new HashMap<>();

		Document doc = DocumentUtils.parse(prevRoundTeamStatsXML);
		NodeList teamNodes = doc.getElementsByTagName(TEAM_ATTR);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			String currentTeamName = getNameFromTeamNode(currentTeam);

			if (currentTeamName.equals(bundesLigaTeam)) {
				teamPerformance = getPrevRoundStats(currentTeam, prevRoundAverageStats);
				break;
			}
		}

		return teamPerformance;
	}

	private static Map<String, Integer> getPrevRoundStats(Node currentTeam, Map<String, Integer> prevRoundAverageStats) {

		Map<String, Integer> prevRoundStats = new LinkedHashMap<>();

		NamedNodeMap defensiveAttributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getAttributes();

		NamedNodeMap offensiveAttributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getFirstChild().getNextSibling().getAttributes();

		NamedNodeMap foulsAttribute =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getFirstChild().getNextSibling().getNextSibling().getNextSibling()
						.getAttributes();

		prevRoundStats.put("distance", getAttribute(TRACK_DIST_ATTR, prevRoundAverageStats, defensiveAttributes));
		prevRoundStats.put("sprints", getAttribute(TRACK_SPRINTS_ATTR, prevRoundAverageStats, defensiveAttributes));
		prevRoundStats.put("passes", getAttribute(TRACK_PASSES_ATTR, prevRoundAverageStats, defensiveAttributes));
		prevRoundStats.put("shots", getAttribute(TRACK_SHOTS_ATTR, prevRoundAverageStats, offensiveAttributes));
		prevRoundStats.put("fouls", getAttribute(TRACK_FOULS_ATTR, prevRoundAverageStats, foulsAttribute));

		return prevRoundStats;
	}

	private static Integer getAttribute(String attrName, Map<String, Integer> prevRoundAverageStats,
			NamedNodeMap defensiveAttributes) {

		String attrValue;

		if (StringUtils.isBlank(defensiveAttributes.getNamedItem(attrName).getNodeValue())) {
			attrValue = prevRoundAverageStats.get(attrName).toString();
		} else {
			attrValue = defensiveAttributes.getNamedItem(attrName).getNodeValue();
		}

		//Get the integer part of the number
		if (attrValue.contains(".")) {
			attrValue = attrValue.substring(0, attrValue.lastIndexOf("."));
		}

		log.debug("{} : {}", attrName, attrValue);
		return Integer.parseInt(attrValue);
	}

	public static Map<String, Integer> getAverageRoundStats(int year, int round,
			Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		URL prevRoundStatsURL =
				URLUtils.createSafeURL(String.format(BUNDESLIGA_DOMAIN + TEAM_STATS_URL, year, round));
		String prevRoundStatsXML = WebCrawler.crawl(prevRoundStatsURL, crawledPages);

		log.debug("Parsing average statistics for round {} year {}", round, year);
		return parseAverageRoundStats(prevRoundStatsXML);
	}

	public static Map<String, Integer> parseAverageRoundStats(String prevRoundStatsXML) {

		Document doc = DocumentUtils.parse(prevRoundStatsXML);
		NodeList teamNodes = doc.getElementsByTagName(GROUP_STATS_ATTR);

		NamedNodeMap statsAttributes = teamNodes.item(0).getAttributes();
		Map<String, Integer> averageStats = new HashMap<>();

		addAttribute(TRACK_DIST_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_SPRINTS_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_PASSES_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_SHOTS_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_FOULS_ATTR, statsAttributes, averageStats);

		return averageStats;
	}

	private static void addAttribute(String attrName, NamedNodeMap statsAttributes,
			Map<String, Integer> averageStats) {
		Node attributeNode = statsAttributes.getNamedItem(attrName);
		String key = attributeNode.getNodeName();
		int value = (int) Double.parseDouble(attributeNode.getNodeValue());
		log.debug("{} : {}", key, value);
		averageStats.put(key, value);
	}

	public static String covertIdToTeamNameFromNode(Node team) {
		Node teamMetaDataNode = team.getFirstChild().getNextSibling();
		int teamId =
				Integer.parseInt(teamMetaDataNode.getAttributes().getNamedItem(TEAM_KEY_ATTR).getNodeValue());
		return TeamsMapping.bundesligaIdToName.get(teamId);
	}

	public static NodeList getMatchTable(int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		String currentRoundMatchesContent = Bundesliga.getMatches(year, round, crawledPages);

		Document currentRoundMatchesXML = DocumentUtils.parse(currentRoundMatchesContent);

		return currentRoundMatchesXML.getElementsByTagName(TEAM_ATTR);
	}
}
