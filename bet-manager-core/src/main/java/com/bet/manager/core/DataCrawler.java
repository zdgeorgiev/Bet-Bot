package com.bet.manager.core;

import com.bet.manager.core.exceptions.IllegalTeamMappingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DataCrawler {

	private static final Logger log = LoggerFactory.getLogger(DataCrawler.class);

	private static final int ROUNDS = 34;

	private static final String BUNDESLIGA_DOMAIN = "http://www.bundesliga.com/";
	private static final String STATS_URL = "data/feed/51/%s/team_stats_round/team_stats_round_%s.xml?cb=544329";
	private static final String ROUND_MATCHES = "data/feed/51/%s/post_standing/post_standing_%s.xml?cb=517837";

	private static final String TRACK_DIST_ATTR = "imp:tracking-distance";
	private static final String TRACK_SPRINTS_ATTR = "imp:tracking-sprints";
	private static final String TRACK_PASSES_ATTR = "imp:passes-total";
	private static final String TRACK_SHOTS_ATTR = "shots-total";
	private static final String TRACK_FOULS_ATTR = "fouls-committed";

	private static final String RESULTDB_DOMAIN = "http://www.resultdb.com/";
	private static final String RESULTDB_MATCHES_FOR_TEAM_URL = "germany/%s/%s/";

	private static final String TABLE_SELECTOR = "table.results";
	private static final String TEAM_ATTR = "team";
	private static final String TEAM_KEY_ATTR = "team-key";
	private static final String CODE_NAME_ATTR = "code-name";
	private static final String CODE_KEY_ATTR = "code-key";
	private static final String CODE_TYPE_ATTR = "code-type";
	private static final String GROUP_STATS_ATTR = "group-stats";
	private static final String TEAM_ID_SPLITERATOR = "soccer.t_";
	private static final String SPORTS_CONTENT_ATTR = "sports-content-code";

	private static final WebCrawler crawler = new WebCrawler();

	private DataCrawler() {
	}

	/**
	 * This method creating data for every round matches during given year in the bundesliga
	 * german football league. This method doing multiple inner crawlings which requiring internet connection.
	 *
	 * @param year for which want to get the data
	 * @return {@link List<String>} containing list of data for every match
	 */
	public static List<String> getDataForAllMacthes(int year) {

		log.info("Start collecting data for year {}", year);

		List<String> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			try {
				List<String> currentRoundEntries = createDataForRound(year, round);
				allData.addAll(currentRoundEntries);
				log.info("Successfully created data for {} matches for round {}.", currentRoundEntries.size(), round);
			} catch (Exception e) {
				log.error("Failed to create data for {} round {}." + System.lineSeparator() + "{}",
						year,
						round,
						e.getMessage());
			}
		}

		log.info("Successfully created {} data entries for year {}", allData.size(), year);
		return allData;
	}

	/**
	 * Creating data for given round and year. This method containing multiple inner
	 * crawlings which requiring internet connection
	 *
	 * @param year
	 * @param round
	 * @return {@link List<String>} matches for current {@param year} and {@param round}
	 * @throws MalformedURLException if the string xml cannot be parsed as document
	 * @throws InterruptedException  if the current thread cannot sleep
	 */
	private static List<String> createDataForRound(int year, int round)
			throws MalformedURLException, InterruptedException {

		String prevRoundMatchesContent = getMatchesContent(year, round - 1);
		String currentRoundMatchesContent = getMatchesContent(year, round);

		Document previousRoundMatchesXML = DocumentUtils.parse(prevRoundMatchesContent);
		Document currentRoundMatchesXML = DocumentUtils.parse(currentRoundMatchesContent);

		Map<String, Integer> prevRoundRanking = parseRoundRanking(previousRoundMatchesXML);
		Map<String, Double> prevRoundAverageStats = parseAverageRoundStats(year, round - 1);

		NodeList teams = currentRoundMatchesXML.getElementsByTagName(TEAM_ATTR);

		List<String> dataRows = new ArrayList<>();
		Set<String> teamBlackList = new HashSet<>();

		for (int i = 0; i < teams.getLength(); i++) {

			StringBuilder currentRowData = new StringBuilder();
			Node currentTeam = teams.item(i);

			String homeTeam = getTeamName(currentTeam);
			String[] awayTeamAndVenue = parseCurrentTeamOpponentAndVenue(homeTeam, year, round);
			String awayTeam = awayTeamAndVenue[0];
			String venue = awayTeamAndVenue[1];

			if (teamBlackList.contains(homeTeam) || teamBlackList.contains(awayTeam)) {
				continue;
			}

			teamBlackList.add(homeTeam);
			teamBlackList.add(awayTeam);

			currentRowData.append(round + " ");

			currentRowData
					.append(parseDataForTeam(homeTeam, previousRoundMatchesXML, year, round,
							prevRoundAverageStats, prevRoundRanking) + " ");
			currentRowData
					.append(parseDataForTeam(awayTeam, previousRoundMatchesXML, year, round,
							prevRoundAverageStats, prevRoundRanking) + " ");

			currentRowData.append(getLastTwoMatchesBetween(homeTeam, awayTeam, previousRoundMatchesXML));

			dataRows.add(currentRowData.toString());
		}

		return dataRows;
	}

	private static String getMatchesContent(int year, int round) throws MalformedURLException {
		URL prevRoundMatchesURL = new URL(String.format(BUNDESLIGA_DOMAIN + ROUND_MATCHES, year, round));
		return crawler.crawl(prevRoundMatchesURL);
	}

	/**
	 * Create ranking map for the teams
	 *
	 * @param doc parsed as xml
	 * @return {@link Map<String, Integer>} containing pairs {team} => {rank}
	 */
	public static Map<String, Integer> parseRoundRanking(Document doc) {

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

				checkIfNameIsCorrespondingToTeam(teamId, teamName);

				ranking.put(teamName, ranking.size() + 1);
			}
		}

		return ranking;
	}

	/**
	 * Method which check if the mapping from {@link BundesLiga} is correct with the
	 * bundesliga crawled id. This is pretty necessary because otherwise the data should be non-corresponding
	 *
	 * @param id   team id
	 * @param team team name
	 * @return true if the mapping is correct, false otherwise
	 */
	private static boolean checkIfNameIsCorrespondingToTeam(int id, String team) {
		if (BundesLiga.teams.get(id).equals(team)) {
			return true;
		}

		log.error("Team with id {} is mapped to {}, but in the xml team node id {} is mapped to {}",
				id, team,
				id, BundesLiga.teams.get(id));

		throw new IllegalTeamMappingException("Invalid mapping found in hash map and the xml team node");
	}

	private static Map<String, Double> parseAverageRoundStats(int year, int round)
			throws MalformedURLException {

		URL prevRoundStatsURL = new URL(String.format(BUNDESLIGA_DOMAIN + STATS_URL, year, round - 1));
		String prevRoundStatsXML = crawler.crawl(prevRoundStatsURL);
		return getAverageRoundStats(prevRoundStatsXML);
	}

	/**
	 * Method which creates {@link Map<String, Double>} with pairs {key} => {value}
	 * for different statistics (track distance, sprints, goals, fouls, passes)
	 *
	 * @param prevRoundStatsXML xml containing the previous round statistics
	 * @return @link Map<String, Double>} with average statistics for given round and year
	 * @throws MalformedURLException if the url cannot be crawled
	 */
	public static Map<String, Double> getAverageRoundStats(String prevRoundStatsXML) {

		Document doc = DocumentUtils.parse(prevRoundStatsXML);
		NodeList teamNodes = doc.getElementsByTagName(GROUP_STATS_ATTR);

		NamedNodeMap statsAttributes = teamNodes.item(0).getAttributes();
		Map<String, Double> averageStats = new HashMap<>();

		addAttribute(TRACK_DIST_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_SPRINTS_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_PASSES_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_SHOTS_ATTR, statsAttributes, averageStats);
		addAttribute(TRACK_FOULS_ATTR, statsAttributes, averageStats);

		return averageStats;
	}

	private static void addAttribute(String attrName, NamedNodeMap statsAttributes, Map<String, Double> averageStats) {
		Node attributeNode = statsAttributes.getNamedItem(attrName);
		averageStats.put(attributeNode.getNodeName(), Double.parseDouble(attributeNode.getNodeValue()));
	}

	private static String getTeamName(Node currentTeam) {
		Node teamMetaDataNode = currentTeam.getFirstChild().getNextSibling();
		int teamId = Integer.parseInt(teamMetaDataNode.getAttributes().getNamedItem(TEAM_KEY_ATTR).getNodeValue());
		return BundesLiga.teams.get(teamId);
	}

	private static String[] parseCurrentTeamOpponentAndVenue(String homeTeam, int year, int round)
			throws MalformedURLException {

		String resultDBTeamName = ResultDB.bundesLigaMappingToResultDB.get(homeTeam);
		URL resultDBUrl =
				new URL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = crawler.crawl(resultDBUrl);

		return getCurrentTeamOpponentAndVenue(content, round);
	}

	/**
	 * Method parses the given html as string and return the target round match opponent and venue
	 * Venue is Away or Home
	 *
	 * @param allMatchesHTML contains the arbitrary team all matches for arbitrary year
	 * @param round          to know which opponent should be returned
	 * @return the opponent and venue stored in array (0 => opponent, 1 => venue)
	 */
	public static String[] getCurrentTeamOpponentAndVenue(String allMatchesHTML, int round) {

		org.jsoup.nodes.Document doc = Jsoup.parse(allMatchesHTML);
		Element e = doc.body().select(TABLE_SELECTOR).get(0);

		int matchesCount = e.children().get(0).children().size();
		Element element = e.children().get(0).children().get(matchesCount - round);

		String opponent = element.children().get(1).text();
		String venue = element.children().get(2).text();

		return new String[] { opponent, venue };
	}

	public static String parseDataForTeam(String teamName, Document doc, int year, int round,
			Map<String, Double> prevRoundAverageStats, Map<String, Integer> prevRoundRanking)
			throws InterruptedException {

		Thread.sleep(new Random().nextInt(5) * 1000);
		return "";
	}

	private static String getLastTwoMatchesBetween(String homeTeam, String awayTeam, Document xmlDocument) {
		return "";
	}
}