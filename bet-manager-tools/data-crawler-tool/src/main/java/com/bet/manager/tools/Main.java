package com.bet.manager.tools;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.data.DataManager;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ISecondarySource;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.models.dao.MatchMetaData;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final int ROUNDS = 2;

	private static DataManager dm;
	private static ISecondarySource secondarySource = new ResultDB();

	private static final Map<URL, String> crawledPages = new HashMap<>();

	public static void main(String[] args) throws IOException {

		dm = new DataManager(false, crawledPages);

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

		File destinationFolder = initializeDestinationFolder(args);

		for (int year = startYear; year <= endYear; year++) {

			List<MatchMetaData> currentYearData = getDataForAllMatches(year);

			if (currentYearData.size() != 0) {

				File textFile = new File(destinationFolder + File.separator + year + "_bundesliga_meta_data.txt");
				FileUtils.writeLines(textFile, currentYearData, true);

				File jsonFile = new File(destinationFolder + File.separator + year + "_bundesliga_meta_data.json");
				FileUtils.writeLines(jsonFile,
						Collections.singleton(objectMapper.writeValueAsString(currentYearData)), true);
			} else
				throw new IllegalStateException("There is no information for year later than " + (year - 1));
		}
	}

	private static File initializeDestinationFolder(String[] args) {

		File destinationFolder;

		if (args.length == 2)
			destinationFolder = new File(args[1]);
		else if (args.length == 3)
			destinationFolder = new File(args[2]);
		else
			throw new IllegalArgumentException("Arguments cannot be less than 2 or more than 3.");

		if (!destinationFolder.exists() && !destinationFolder.mkdirs())
			throw new IllegalStateException("Cannot create the destination folder " + destinationFolder.getAbsolutePath());

		return destinationFolder;
	}

	/**
	 * This method creating data for every round matches during given year in the bundesliga
	 * german football league. This method doing multiple inner crawlings which requiring internet connection.
	 *
	 * @param year for which want to get the data. The year should be at least 2011
	 * @return {@link List <MatchMetaData> } containing all match meta data
	 */
	private static List<MatchMetaData> getDataForAllMatches(int year) throws MalformedURLException {

		long startTime = System.currentTimeMillis();

		List<MatchMetaData> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			log.info("Start collecting data for year {} round {}", year, round);
			allData.addAll(createDataForRound(year, round));
			crawledPages.clear();
		}

		long finishTime = System.currentTimeMillis();
		String elapsedTime = PerformanceUtils.convertToHumanReadable(finishTime - startTime);

		log.info("Successfully created {} data entries for year {}. Finished in {}", allData.size(), year, elapsedTime);
		return allData;
	}

	private static List<MatchMetaData> createDataForRound(int year, int round) {

		Set<String> teamBlackList = new HashSet<>();
		List<MatchMetaData> currentData = new ArrayList<>();

		try {
			NodeList currentRoundTeams = Bundesliga.getMatchTable(year, round, crawledPages);

			for (int i = 0; i < currentRoundTeams.getLength(); i++) {

				Node currentTeam = currentRoundTeams.item(i);

				String homeTeam = Bundesliga.covertIdToTeamNameFromNode(currentTeam);
				String awayTeam = secondarySource.getTeamOpponent(homeTeam, year, round, crawledPages);

				if (!teamBlackList.contains(homeTeam) && !teamBlackList.contains(awayTeam)) {

					teamBlackList.add(homeTeam);
					teamBlackList.add(awayTeam);

					MatchMetaData matchMetaData = dm.getDataForMatch(homeTeam, awayTeam, year, round);

					log.info("({}/{}) Data for match '{}'-'{}' was successfully created",
							currentData.size() + 1, currentRoundTeams.getLength() / 2, homeTeam, awayTeam);

					currentData.add(matchMetaData);
				}
			}
		} catch (Exception e) {
			log.error("Failed to create data for year {} round {}.", year, round, e);
		}

		log.info("Successfully created data for {} matches for year {} round {}.", currentData.size(), year, round);
		return currentData;
	}
}
