package com.bet.manager.core.data;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.core.util.DocumentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DataManger {

	private static final Logger log = LoggerFactory.getLogger(DataManger.class);

	private static final int ROUNDS = 34;

	private static final String TEAM_ATTR = "team";

	private static final Map<URL, String> crawledPages = new HashMap<>();

	private DataManger() {
	}

	/**
	 * This method creating data for every round matches during given year in the bundesliga
	 * german football league. This method doing multiple inner crawlings which requiring internet connection.
	 *
	 * @param year for which want to get the data. The year should be at least 2011
	 * @return {@link List<String>} containing list of data for every match
	 */
	public static List<String> getDataForAllMatches(int year) {

		log.info("Start collecting data for year {}", year);
		long startTime = System.currentTimeMillis();

		List<String> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			try {
				crawledPages.clear();
				List<String> currentRoundEntries = createDataForRound(year, round);
				allData.addAll(currentRoundEntries);
			} catch (Exception e) {
				log.error("Failed to create data for year {} round {}.", year, round, e);
			}
		}

		long finishTime = System.currentTimeMillis();
		String elapsedTime = PerformanceUtils.convertToHumanReadable(finishTime - startTime);

		log.info("Successfully created {} data entries for year {}. Finished in {}", allData.size(), year, elapsedTime);
		return allData;
	}

	private static List<String> createDataForRound(int year, int round)
			throws MalformedURLException, InterruptedException {

		log.info("({}/{}) Creating data for year {} round {}", round - 1, ROUNDS - 1, year, round);

		String currentRoundMatchesContent = Bundesliga.getMatches(year, round, crawledPages);

		Document currentRoundMatchesXML = DocumentUtils.parse(currentRoundMatchesContent);

		Map<String, Integer> currentRoundRanking = Bundesliga.getRoundRanking(currentRoundMatchesXML);
		Map<String, Integer> prevRoundAverageStats = Bundesliga.parseAverageRoundStats(year, round - 1, crawledPages);

		NodeList teams = currentRoundMatchesXML.getElementsByTagName(TEAM_ATTR);

		List<String> dataRows = new ArrayList<>();
		Set<String> teamBlackList = new HashSet<>();

		for (int i = 0; i < teams.getLength(); i++) {

			StringBuilder currentRowData = new StringBuilder();
			Node currentTeam = teams.item(i);

			String homeTeam = Bundesliga.getTeamNameFromId(currentTeam);
			String[] opponentTeamAndVenue =
					ResultDB.parseCurrentTeamOpponentAndVenue(homeTeam, year, round, crawledPages);

			String opponentTeam = opponentTeamAndVenue[0];

			String currentTeamVenue = opponentTeamAndVenue[1];
			String opponentVenue = currentTeamVenue.equals("1") ? "-1" : "1";

			if (teamBlackList.contains(homeTeam) || teamBlackList.contains(opponentTeam)) {
				continue;
			}

			teamBlackList.add(homeTeam);
			teamBlackList.add(opponentTeam);

			log.info("Creating data for match {} '{}' - '{}' in year {} round {}",
					dataRows.size() + 1, homeTeam, opponentTeam, year, round);

			currentRowData
					.append(round)
					.append(" ")
					.append(getDataForTeam(homeTeam, currentRoundMatchesXML, year, round, currentTeamVenue,
							prevRoundAverageStats, currentRoundRanking))
					.append(" ")
					.append(getDataForTeam(opponentTeam, currentRoundMatchesXML, year, round, opponentVenue,
							prevRoundAverageStats, currentRoundRanking))
					.append(" ")
					.append(Bundesliga.getMatchResult(homeTeam, opponentTeam, currentRoundMatchesXML));

			log.info("Data for match {} was successfully created", dataRows.size() + 1, homeTeam, opponentTeam);
			dataRows.add(currentRowData.toString());
		}

		log.info("Successfully created data for {} matches for year {} round {} with total crawled pages {}.",
				dataRows.size(), year, round, crawledPages.size());
		return dataRows;
	}

	/**
	 * Method which will be used to collect data for given team for current year and specific found.
	 * This method containing multiple inner crawling, so its requiring internet connection.
	 *
	 * @param team                  which we want to get data. The name should be in Bundesliga name format.
	 * @param currentRoundStats     previous round matches information as {@link Document}
	 * @param year                  for which we want to get information
	 * @param round                 for which we want to get information
	 * @param venue                 for current team and its Home(1) or Away(-1), depends for the match
	 * @param prevRoundAverageStats average stats for the previous round. This is useful if the crawled data is
	 *                              not fully included and some of the fields are left empty, if so the average
	 *                              data will be used instead.
	 * @param currentRoundRanking   Position in the ranking for the current and round
	 * @return all of the data information as {@link String}
	 */
	public static String getDataForTeam(String team, Document currentRoundStats, int year, int round,
			String venue, Map<String, Integer> prevRoundAverageStats, Map<String, Integer> currentRoundRanking)
			throws MalformedURLException, InterruptedException {

		StringBuilder currentTeamData = new StringBuilder();

		currentTeamData
				.append(Bundesliga.parseTeamRankingPlace(team, currentRoundRanking))
				.append(" ")
				.append(Bundesliga.parseCurrentRankingStats(team, currentRoundStats))
				.append(" ")
				.append(venue)
				.append(" ")
				.append(Bundesliga
						.parsePrevRoundTeamPerformance(team, year, round, prevRoundAverageStats, crawledPages))
				.append(" ")
				.append(ResultDB.parseResultsForPastFiveGames(team, year, round, crawledPages));

		return currentTeamData.toString();
	}
}