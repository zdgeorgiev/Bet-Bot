package com.bet.manager.tools;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.data.DataManager;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ISecondarySource;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	private static final int ROUNDS = 34;

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
		} else {
			throw new IllegalStateException("Less arguments than required.");
		}

		File destinationFolder = initializeDestinationFolder(args);

		for (int year = startYear; year <= endYear; year++) {

			List<FootballMatch> currentYearData = getAllMatches(year);

			if (currentYearData.size() != 0) {

				File textFile = new File(destinationFolder + File.separator + year + "_bundesliga_matches_metadata.txt");
				for (FootballMatch footballMatch : currentYearData)
					FileUtils.write(textFile, footballMatch.getMetaDataNNOutput() + System.lineSeparator(), true);

				File jsonFile = new File(destinationFolder + File.separator + year + "_bundesliga_matches.json");
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
	 * This method creating matches for given year in the bundesliga german football league.
	 * This method is using crawling which requiring network connection.
	 *
	 * @param year for which want to get the data. The year should be at least 2011
	 * @return {@link List <{@link FootballMatch}> } containing all matches for the year
	 */
	private static List<FootballMatch> getAllMatches(int year) throws MalformedURLException {

		long startTime = System.currentTimeMillis();

		List<FootballMatch> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			log.info("Start collecting data for year {} round {}", year, round);

			try {
				allData.addAll(createMatchesForRound(year, round));
			} catch (Exception e) {
				log.error("Failed to create matches", e);
			}

			crawledPages.clear();
		}

		long finishTime = System.currentTimeMillis();
		String elapsedTime = PerformanceUtils.convertToHumanReadable(finishTime - startTime);

		log.info("Successfully created {} match entries for year {}. Finished in {}", allData.size(), year, elapsedTime);
		return allData;
	}

	private static List<FootballMatch> createMatchesForRound(int year, int round) {

		Set<String> teamBlackList = new HashSet<>();
		List<FootballMatch> currentData = new ArrayList<>();
		NodeList currentRoundTeams;

		try {
			currentRoundTeams = Bundesliga.getMatchTable(year, round, crawledPages);
		} catch (Exception e) {
			throw new IllegalStateException(String.format("Failed to create match table for %s year %s round", year, round));
		}

		for (int i = 0; i < currentRoundTeams.getLength(); i++) {

			try {
				Node currentTeam = currentRoundTeams.item(i);

				String firstTeam = Bundesliga.covertIdToTeamNameFromNode(currentTeam);
				String secondTeam = secondarySource.getTeamOpponent(firstTeam, year, round, crawledPages);

				if (!teamBlackList.contains(firstTeam) && !teamBlackList.contains(secondTeam)) {

					teamBlackList.add(firstTeam);
					teamBlackList.add(secondTeam);

					FootballMatch currentMatch = dm.createFootballMatch(firstTeam, secondTeam, year, round);
					currentMatch.setMatchStatus(MatchStatus.FINISHED);

					log.info("({}/{}) Match '{}'-'{}' was successfully created", currentData.size() + 1,
							currentRoundTeams.getLength() / 2, currentMatch.getHomeTeam(), currentMatch.getAwayTeam());

					currentData.add(currentMatch);
				}
			} catch (Exception e) {
				log.error("Failed to create matches for year {} round {}.", year, round, e);
			}
		}

		log.info("Successfully created {} matches for year {} round {}.", currentData.size(), year, round);
		return currentData;
	}
}
