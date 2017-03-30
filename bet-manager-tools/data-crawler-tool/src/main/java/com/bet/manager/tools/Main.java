package com.bet.manager.tools;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.data.FootballDataManager;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.FootballDataUtils;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.exceptions.MetaDataCreationException;
import com.bet.manager.model.util.FootballMatchBuilder;
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

	private static FootballDataManager dm;

	private static final Map<URL, String> crawledPages = new HashMap<>();

	public static void main(String[] args) throws IOException {

		dm = new FootballDataManager(crawledPages);

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

		File destinationFolder = initDestinationFolder(args);

		for (int year = startYear; year <= endYear; year++) {

			List<FootballMatch> currentYearData = getAllMatches(year);

			if (currentYearData.size() != 0) {

				File nnInput = new File(destinationFolder + File.separator + year + "_bundesliga_matches_nn_input.txt");
				writeMatchesNNInput(currentYearData, nnInput);

				File jsonFile = new File(destinationFolder + File.separator + year + "_bundesliga_matches.json");
				FileUtils.writeLines(jsonFile, Collections.singleton(objectMapper.writeValueAsString(currentYearData)), true);
			} else
				throw new MetaDataCreationException("There is no information for year later than " + (year - 1));
		}
	}

	private static File initDestinationFolder(String[] args) {

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
	 * This method create matches for given year in the bundesliga german football league.
	 * This method is using crawling which requiring network connection.
	 *
	 * @param matches         {@link List <{@link FootballMatch}> } contains football matches
	 * @param destinationFile is the output file to write on
	 */
	private static void writeMatchesNNInput(List<FootballMatch> matches, File destinationFile) throws IOException {

		for (FootballMatch footballMatch : matches) {
			FileUtils.write(destinationFile, footballMatch.getMetaDataNNInput() + MatchMetaData.SPLITERATOR
					+ footballMatch.getResult() + System.lineSeparator(), true);
		}
	}

	/**
	 * This method create matches for given year in the bundesliga german football league.
	 * This method is using crawling which requiring network connection.
	 *
	 * @param year for which you want to get the data. Year must be at least 2011
	 * @return {@link List <{@link FootballMatch}> } containing all matches for the year
	 */
	private static List<FootballMatch> getAllMatches(int year) throws MalformedURLException {

		long startTime = System.currentTimeMillis();

		List<FootballMatch> allData = new ArrayList<>();

		// We skip the first round because for the current match we only looking for the previous one data
		for (int round = 2; round <= ROUNDS; round++) {

			log.info("Start collecting data for year {} round {}", year, round);

			allData.addAll(createMatchesForRound(year, round));

			crawledPages.clear();
		}

		long finishTime = System.currentTimeMillis();
		String elapsedTime = PerformanceUtils.convertToHumanReadable(finishTime - startTime);

		log.info("Successfully created {} match entries for year {}. Finished in {}", allData.size(), year, elapsedTime);
		return allData;
	}

	/**
	 * This method create matches for specific year and round in the bundesliga german football league.
	 * This method is using crawling which requiring network connection.
	 *
	 * @param year  for which you want to get the data. Year must be at least 2011
	 * @param round for which round you want to get the data. Round must be at least 2
	 * @return {@link List <{@link FootballMatch}> } containing all matches for the year
	 */
	private static List<FootballMatch> createMatchesForRound(int year, int round) {

		Set<String> teamBlackList = new HashSet<>();
		List<FootballMatch> currentData = new ArrayList<>();
		NodeList currentRoundTeams;

		try {
			currentRoundTeams = Bundesliga.getMatchTable(year, round, crawledPages);
		} catch (Exception e) {
			throw new MetaDataCreationException(String.format("Failed to create match table for %s year %s round", year, round));
		}

		String firstTeam = null;
		String secondTeam = null;
		int matchIndex = 0;

		for (int i = 0; i < currentRoundTeams.getLength(); i++) {

			try {
				Node currentTeam = currentRoundTeams.item(i);

				firstTeam = Bundesliga.covertIdToTeamNameFromNode(currentTeam);
				secondTeam = FootballDataUtils.getTeamOpponent(firstTeam, year, round, crawledPages);

				if (!teamBlackList.contains(firstTeam) && !teamBlackList.contains(secondTeam)) {

					matchIndex++;

					teamBlackList.add(firstTeam);
					teamBlackList.add(secondTeam);

					FootballMatch currentMatch = new FootballMatchBuilder()
							.setHomeTeamName(firstTeam)
							.setAwayTeamName(secondTeam)
							.setYear(year)
							.setRound(round)
							.build();

					dm.createData(currentMatch);

					currentMatch = new FootballMatchBuilder(currentMatch)
							.setStatus(MatchStatus.FINISHED)
							.build();

					log.info("({}/{}) Match '{}'-'{}' was successfully created", matchIndex,
							currentRoundTeams.getLength() / 2, currentMatch.getHomeTeam(), currentMatch.getAwayTeam());

					currentData.add(currentMatch);
				}
			} catch (Exception e) {
				log.error("Failed to create match {} [{}] - [{}] for year {} round {}.",
						matchIndex, firstTeam, secondTeam, year, round, e);
			}
		}

		log.info("Successfully created {} matches for year {} round {}.", currentData.size(), year, round);
		return currentData;
	}
}
