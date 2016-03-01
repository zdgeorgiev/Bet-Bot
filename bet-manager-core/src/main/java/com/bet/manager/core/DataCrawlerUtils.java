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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DataCrawlerUtils {

	private static final Logger log = LoggerFactory.getLogger(DataCrawlerUtils.class);

	private static final int ROUNDS = 34;
	private static final String BUNDESLIGA_DOMAIN = "http://www.bundesliga.com/";
	private static final String STATS_URL = "data/feed/51/%s/team_stats_round/team_stats_round_%s.xml?cb=544329";
	private static final String ROUND_MATCHES = "data/feed/51/%s/post_standing/post_standing_%s.xml?cb=517837";

	private static final String RESULTDB_DOMAIN = "http://www.resultdb.com/";
	private static final String RESULTDB_MATCHES_FOR_TEAM_URL = "germany/%s/%s/";

	private static final DocumentBuilder dBuilder;
	private static final DocumentBuilderFactory dbFactory;

	private static final WebCrawler crawler = new WebCrawler();

	static {
		dbFactory = DocumentBuilderFactory.newInstance();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException("Failed to create document builder.");
		}
	}

	private DataCrawlerUtils() {
	}

	public static List<String> getDataForAllMacthes(int year)
			throws IOException, SAXException, ParserConfigurationException {

		log.info("Start collecting data for year {}", year);

		List<String> allData = new ArrayList<>();

		// We skip the first round because all the data does not matter to the matches outcome
		for (int round = 2; round <= ROUNDS; round++) {

			URL roundMatchesURL = new URL(String.format(BUNDESLIGA_DOMAIN + ROUND_MATCHES, year, round - 1));
			String roundMatchesXML = crawler.crawl(roundMatchesURL);
			List<String> matchesDataForCurrentRound = createDataForRound(roundMatchesXML, year, round);

			allData.addAll(matchesDataForCurrentRound);

			log.info("Successfully created data for {} matches for round {}.",
					matchesDataForCurrentRound.size(),
					round);
		}

		return allData;
	}

	public static List<String> createDataForRound(String prevRoundMatches, int year, int round)
			throws IOException, SAXException {

		Document prevRoundMatchesXML = parseDocFromXML(prevRoundMatches);

		Map<String, Integer> roundRanking = parseRoundRanking(prevRoundMatchesXML);
		Map<String, Double> roundStats = parseRoundStats(year, round);

		NodeList teams = prevRoundMatchesXML.getElementsByTagName("team");

		List<String> dataRows = new ArrayList<>();
		Set<String> teamBlackList = new HashSet<>();

		for (int i = 0; i < teams.getLength(); i++) {

			StringBuilder currentRowData = new StringBuilder();
			Node currentTeam = teams.item(i);

			String homeTeam = getTeamName(currentTeam);
			String awayTeam = getCurrentTeamOpponent(homeTeam, year, round);

			if (teamBlackList.contains(homeTeam) || teamBlackList.contains(awayTeam)) {
				continue;
			}

			teamBlackList.add(homeTeam);
			teamBlackList.add(awayTeam);

			currentRowData.append(round + " ");
			currentRowData
					.append(getDataForTeam(homeTeam, prevRoundMatchesXML, year, round, roundStats, roundRanking) + " ");
			//Thread.seep (3-5 seconds)
			currentRowData
					.append(getDataForTeam(awayTeam, prevRoundMatchesXML, year, round, roundStats, roundRanking) + " ");
			//Thread.seep (3-5 seconds)
			currentRowData.append(getLastTwoMatchesBetween(homeTeam, awayTeam, prevRoundMatchesXML));
			//Thread.seep (3-5 seconds)

			dataRows.add(currentRowData.toString());
		}

		return dataRows;
	}

	private static Document parseDocFromXML(String xml) throws SAXException, IOException {
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml));

		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		return doc;
	}

	private static Map<String, Integer> parseRoundRanking(Document doc) {

		Map<String, Integer> ranking = new HashMap<>();
		NodeList teamNodes = doc.getElementsByTagName("sports-content-code");

		for (int i = 1; i <= teamNodes.getLength(); i++) {

			Node currentTeam = teamNodes.item(i - 1);
			NamedNodeMap attributes = currentTeam.getAttributes();

			if (attributes.getNamedItem("code-type").getNodeValue().equals("team")) {

				int teamId = Integer.parseInt(attributes.getNamedItem("code-key").getNodeValue().split("soccer.t_")[1]);
				String teamName = attributes.getNamedItem("code-name").getNodeValue();

				checkIfNameIsCorrespondingToTeam(teamId, teamName);

				ranking.put(teamName, i);
			}
		}

		return ranking;
	}

	private static Map<String, Double> parseRoundStats(int year, int round) throws MalformedURLException {

		URL prevRoundStatsURL = new URL(String.format(BUNDESLIGA_DOMAIN + STATS_URL, year, round - 1));
		String prevRoundStatsXML = crawler.crawl(prevRoundStatsURL);

		//		<statistic>
		//			<group>
		//			<group-metadata group-key="51"/>
		//			<group-stats date-coverage-type="season-average" imp:tracking-distance="111923.72" imp:tracking-sprints="191.69" imp:passes-total="393.78" imp:passes-completed="301.48" imp:balls-touched="588.76" imp:duels-total="216.22" imp:duels-won="108.11" shots-total="12.54" offsides="2.65" corner-kicks="4.76" imp:crosses="8.20" fouls-committed="14.94"/>
		//			</group>
		//		</statistic>

		return null;
	}

	private static String getTeamName(Node currentTeam) {
		Node teamMetaDataNode = currentTeam.getFirstChild().getNextSibling();
		int teamId = Integer.parseInt(teamMetaDataNode.getAttributes().getNamedItem("team-key").getNodeValue());
		return BundesLiga.teams.get(teamId);
	}

	private static String getCurrentTeamOpponent(String homeTeam, int year, int round) throws MalformedURLException {

		String resultDBTeamName = ResultDB.bundesLigaMappingToResultDB.get(homeTeam);
		URL resultDBUrl =
				new URL(String.format(RESULTDB_DOMAIN + RESULTDB_MATCHES_FOR_TEAM_URL, resultDBTeamName, year));

		String content = crawler.crawl(resultDBUrl);

		org.jsoup.nodes.Document doc = Jsoup.parse(content);
		Element e = doc.body().select("table.results").get(0);

		int matchesCount = e.children().get(0).children().size();
		Element element = e.children().get(0).children().get(matchesCount - round);

		return element.children().get(1).text();
	}

	private static boolean checkIfNameIsCorrespondingToTeam(int id, String team) {
		if (BundesLiga.teams.get(id).equals(team)) {
			return true;
		}

		log.error("Team with id {} is mapped to {}, but in the xml team node id {} is mapped to {}",
				id, team,
				id, BundesLiga.teams.get(id));

		throw new IllegalTeamMappingException("Invalid mapping found in hash map and the xml team node");
	}

	private static String getDataForTeam(String teamName, Document doc, int year, int round,
			Map<String, Double> prevRoundStats, Map<String, Integer> roundRanking) {

		return "";
	}

	private static String getLastTwoMatchesBetween(String homeTeam, String awayTeam, Document xmlDocument) {
		return "";
	}
}