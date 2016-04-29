package com.bet.manager.tools;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.data.DataManger;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ResultDB;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static final int ROUNDS = 34;

	private static final Map<URL, String> crawledPages = new HashMap<>();

	public static void main(String[] args) throws IOException {

		int startYear;
		int endYear;

		if (args.length == 2) {
			startYear = Integer.parseInt(args[0]);
			endYear = startYear;
		} else if (args.length == 3) {
			startYear = Integer.parseInt(args[0]);
			endYear = Integer.parseInt(args[1]);
		} else
			throw new IllegalStateException("Less arguments than required.");

		if (startYear < 2011)
			throw new IllegalArgumentException("Start year cannot be less than 2011");

		File destinationFolder = initializeDestinationFolder(args);

		for (int year = startYear; year <= endYear; year++) {

			List<String> currentYearData = getDataForAllMatches(year);

			if (currentYearData.size() != 0) {
				File file = new File(destinationFolder + File.separator + year + "_bundesliga_stats.txt");
				FileUtils.writeLines(file, currentYearData, true);
			} else {
				throw new IllegalStateException("There is no information for year later than " + (year - 1));
			}
		}
	}

	private static File initializeDestinationFolder(String[] args) {

		File destinationFolder;

		if (args.length == 2) {
			destinationFolder = new File(args[1]);
		} else if (args.length == 3) {
			destinationFolder = new File(args[2]);
		} else {
			throw new IllegalArgumentException("Arguments cannot be less than 2 or more than 3.");
		}

		if (!destinationFolder.exists() && !destinationFolder.mkdirs())
			throw new IllegalStateException(
					"Cannot create the destination folder " + destinationFolder.getAbsolutePath());

		return destinationFolder;
	}

	/**
	 * This method creating data for every round matches during given year in the bundesliga
	 * german football league. This method doing multiple inner crawlings which requiring internet connection.
	 *
	 * @param year for which want to get the data. The year should be at least 2011
	 * @return {@link List <String>} containing list of data for every match
	 */
	private static List<String> getDataForAllMatches(int year) throws MalformedURLException {

		log.info("Start collecting data for year {}", year);
		long startTime = System.currentTimeMillis();

		List<String> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			List<String> currentRoundEntries = createDataForRound(year, round);
			allData.addAll(currentRoundEntries);
			clearCaches();
		}

		long finishTime = System.currentTimeMillis();
		String elapsedTime = PerformanceUtils.convertToHumanReadable(finishTime - startTime);

		log.info("Successfully created {} data entries for year {}. Finished in {}", allData.size(), year,
				elapsedTime);
		return allData;
	}

	private static List<String> createDataForRound(int year, int round) {

		List<String> dataRows = new ArrayList<>();
		Set<String> teamBlackList = new HashSet<>();

		try {
			NodeList currentRoundTeams = Bundesliga.getMatchTable(year, round, crawledPages);

			for (int i = 0; i < currentRoundTeams.getLength(); i++) {

				Node currentTeam = currentRoundTeams.item(i);

				String homeTeam = Bundesliga.covertIdToTeamNameFromNode(currentTeam);
				String awayTeam = ResultDB.getTeamOpponentAndVenue(homeTeam, year, round, crawledPages)[0];

				if (!teamBlackList.contains(homeTeam) && !teamBlackList.contains(awayTeam)) {

					teamBlackList.add(homeTeam);
					teamBlackList.add(awayTeam);

					StringBuilder currentRowData = new StringBuilder();

					currentRowData
							.append(DataManger
									.getDataForMatch(homeTeam, awayTeam, year, round, crawledPages))
							.append(" ")
							.append(ResultDB.getMatchResult(homeTeam, awayTeam));

					log.info("({}/{}) Data for match '{}'-'{}' was successfully created",
							dataRows.size() + 1, currentRoundTeams.getLength() / 2, homeTeam, awayTeam);
					dataRows.add(currentRowData.toString());
				}
			}
		} catch (Exception e) {
			log.error("Failed to create data for year {} round {}.", year, round, e);
		}

		log.info("Successfully created data for {} matches for year {} round {} with total crawled pages {}.",
				dataRows.size(), year, round, crawledPages.size());
		return dataRows;
	}

	private static void clearCaches() {
		crawledPages.clear();
	}
}
