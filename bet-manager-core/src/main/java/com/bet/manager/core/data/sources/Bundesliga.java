package com.bet.manager.core.data.sources;

import com.bet.manager.core.TeamsMapping;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.exceptions.IllegalTeamMappingException;
import com.bet.manager.core.util.DocumentUtils;
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
import java.util.Map;
import java.util.Random;

public class Bundesliga {

	private static final Logger log = LoggerFactory.getLogger(Bundesliga.class);

	private static final String BUNDESLIGA_DOMAIN = "http://www.bundesliga.com/";
	private static final String STATS_URL = "data/feed/51/%s/team_stats_round/team_stats_round_%s.xml?cb=544329";
	private static final String ROUND_MATCHES_URL = "data/feed/51/%s/post_standing/post_standing_%s.xml?cb=517837";
	private static final String TEAM_STATS_URL = "data/feed/51/%s/team_stats_round/team_stats_round_%s.xml?cb=544329";

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

	private static final WebCrawler crawler = new WebCrawler();

	private Bundesliga() {
	}

	public static Integer parseTeamRankingPlace(String team, Map<String, Integer> currentRoundRanking) {
		return currentRoundRanking.get(team);
	}

	/**
	 * Get the current points and round goals difference for team
	 *
	 * @param team                Team for which you want to get the stats
	 * @param currentRoundMatches Document containing the current round matches
	 * @return
	 */
	public static String parseCurrentRankingStats(String team, Document currentRoundMatches) {

		StringBuilder prevRoundStats = new StringBuilder();

		NodeList teamNodes = currentRoundMatches.getElementsByTagName(TEAM_ATTR);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			String currentTeamName = getNameFromTeamNode(currentTeam);

			if (currentTeamName.equals(team)) {
				prevRoundStats.append(getGoalDifferenceAndPoints(currentTeam));
				break;
			}
		}

		return prevRoundStats.toString();
	}

	private static String getNameFromTeamNode(Node team) {
		return team.getFirstChild().getNextSibling().getFirstChild().getNextSibling().getAttributes()
				.getNamedItem("full").getNodeValue();
	}

	private static String getGoalDifferenceAndPoints(Node currentTeam) {

		NamedNodeMap attributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getAttributes();

		Integer goalDifference =
				Integer.parseInt(attributes.getNamedItem("points-scored-for").getNodeValue()) -
						Integer.parseInt(attributes.getNamedItem("points-scored-against").getNodeValue());

		String output = attributes.getNamedItem("standing-points").getNodeValue() + " " + goalDifference;
		return output;
	}

	public static String parsePrevRoundTeamPerformance(String team, int year, int round,
			Map<String, Integer> prevRoundAverageStats, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		URL prevRoundStatsURL = new URL(String.format(BUNDESLIGA_DOMAIN + TEAM_STATS_URL, year, round - 1));
		String prevRoundTeamStatsXML = getContentOfPage(prevRoundStatsURL, crawledPages);
		return getPrevRoundTeamPerformance(prevRoundTeamStatsXML, team, prevRoundAverageStats);
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
	 * Get previous performance for team and collects the data about total distance, sprints,
	 * passes, shots, fouls and if any of them is leaved empty the value
	 * from the average round stats will come in place
	 *
	 * @param prevRoundTeamStatsXML represent the prev round statistics as xml
	 * @param team                  which wanna get performance
	 * @param prevRoundAverageStats {@link Map<String, Double>} containing the average values
	 * @return
	 */
	public static String getPrevRoundTeamPerformance(String prevRoundTeamStatsXML, String team,
			Map<String, Integer> prevRoundAverageStats) {

		StringBuilder prevRoundStats = new StringBuilder();

		Document doc = DocumentUtils.parse(prevRoundTeamStatsXML);
		NodeList teamNodes = doc.getElementsByTagName(TEAM_ATTR);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			String currentTeamName = getNameFromTeamNode(currentTeam);

			if (currentTeamName.equals(team)) {
				prevRoundStats.append(getPrevRoundStats(currentTeam, prevRoundAverageStats));
				break;
			}
		}

		return prevRoundStats.toString();
	}

	private static String getPrevRoundStats(Node currentTeam, Map<String, Integer> prevRoundAverageStats) {

		StringBuilder output = new StringBuilder();

		NamedNodeMap defensiveAttributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getAttributes();

		output.append(getAttribute(TRACK_DIST_ATTR, prevRoundAverageStats, defensiveAttributes)).append(" ");
		output.append(getAttribute(TRACK_SPRINTS_ATTR, prevRoundAverageStats, defensiveAttributes)).append(" ");
		output.append(getAttribute(TRACK_PASSES_ATTR, prevRoundAverageStats, defensiveAttributes)).append(" ");

		NamedNodeMap offensiveAttributes =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getFirstChild().getNextSibling().getAttributes();

		output.append(getAttribute(TRACK_SHOTS_ATTR, prevRoundAverageStats, offensiveAttributes)).append(" ");

		NamedNodeMap foulsAttribute =
				currentTeam.getFirstChild().getNextSibling().getNextSibling().getNextSibling().getFirstChild()
						.getNextSibling().getFirstChild().getNextSibling().getNextSibling().getNextSibling()
						.getAttributes();

		output.append(getAttribute(TRACK_FOULS_ATTR, prevRoundAverageStats, foulsAttribute));

		return output.toString().trim();
	}

	private static String getAttribute(String attrName, Map<String, Integer> prevRoundAverageStats,
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

		return attrValue;
	}

	public static String getMatches(int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {
		URL prevRoundMatchesURL = new URL(String.format(BUNDESLIGA_DOMAIN + ROUND_MATCHES_URL, year, round));
		return getContentOfPage(prevRoundMatchesURL, crawledPages);
	}

	public static Map<String, Integer> parseAverageRoundStats(int year, int round, Map<URL, String> crawledPages)
			throws MalformedURLException, InterruptedException {

		URL prevRoundStatsURL = new URL(String.format(BUNDESLIGA_DOMAIN + STATS_URL, year, round));
		String prevRoundStatsXML = getContentOfPage(prevRoundStatsURL, crawledPages);

		return getAverageRoundStats(prevRoundStatsXML);
	}

	/**
	 * Method which creates {@link Map<String, Double>} with pairs {key} => {value}
	 * for different statistics (track distance, sprints, goals, fouls, passes)
	 *
	 * @param prevRoundStatsXML xml containing the previous round statistics
	 * @return @link Map<String, Double>} with average statistics for given round and year
	 */
	public static Map<String, Integer> getAverageRoundStats(String prevRoundStatsXML) {

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

	private static void addAttribute(String attrName, NamedNodeMap statsAttributes, Map<String, Integer> averageStats) {
		Node attributeNode = statsAttributes.getNamedItem(attrName);
		averageStats.put(attributeNode.getNodeName(), (int) Double.parseDouble(attributeNode.getNodeValue()));
	}

	/**
	 * Create ranking map for the bundesligaIdToName
	 *
	 * @param doc parsed as xml
	 * @return {@link Map<String, Integer>} containing pairs {team} => {rank}
	 */
	public static Map<String, Integer> getRoundRanking(Document doc) {

		Map<String, Integer> ranking = new HashMap<>();
		NodeList teamNodes = doc.getElementsByTagName(SPORTS_CONTENT_ATTR);

		for (int i = 0; i < teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i);
			NamedNodeMap attributes = currentTeam.getAttributes();

			if (attributes.getNamedItem(CODE_TYPE_ATTR).getNodeValue().equals(TEAM_ATTR)) {

				int teamId =
						Integer.parseInt(
								attributes.getNamedItem(CODE_KEY_ATTR).getNodeValue().split(TEAM_ID_SPLITERATOR)[1]);
				String teamName = attributes.getNamedItem(CODE_NAME_ATTR).getNodeValue();

				checkForValidMappingBundesligaIdToTeam(teamId, teamName);

				ranking.put(teamName, ranking.size() + 1);
			}
		}

		return ranking;
	}

	private static boolean checkForValidMappingBundesligaIdToTeam(int id, String team) {
		if (TeamsMapping.bundesligaIdToName.get(id).equals(team)) {
			return true;
		}

		throw new IllegalTeamMappingException("Team with id " + id + " is mapped to " + team +
				", but in the xml team node id " + id + " is mapped to " + TeamsMapping.bundesligaIdToName.get(id));
	}

	public static String getTeamNameFromId(Node currentTeam) {
		Node teamMetaDataNode = currentTeam.getFirstChild().getNextSibling();
		int teamId = Integer.parseInt(teamMetaDataNode.getAttributes().getNamedItem(TEAM_KEY_ATTR).getNodeValue());
		return TeamsMapping.bundesligaIdToName.get(teamId);
	}

	public static String getMatchResult(String homeTeam, String awayTeam, Document currentRoundRanking) {
		//TODO: Implement the function which extract the end result for given teams
		return null;
	}
}
