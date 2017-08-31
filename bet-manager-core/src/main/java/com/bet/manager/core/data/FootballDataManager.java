package com.bet.manager.core.data;

import com.bet.manager.model.entity.MatchVenueType;
import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchMetaData;
import com.bet.manager.model.exceptions.MetaDataCreationException;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.bet.manager.core.data.sources.FootballDataUtils.*;

public class FootballDataManager implements DataManager<FootballMatch> {

	private static final Logger LOG = LoggerFactory.getLogger(FootballDataManager.class);

	private static final int MIN_ROUND = 1;
	private static final int MAX_ROUND = 34;
	private static final int MIN_YEAR = 2011;
	private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);

	private static final String POSITION = "position";
	private static final String POINTS = "points";
	private static final String GOAL_DIFFERENCE = "goalDifference";
	private static final String VENUE = "venue";
	private static final String PREVIOUS_ROUND_STATS = "previousRoundStats";
	private static final String LAST_FIVE_GAMES = "lastFiveGames";

	private Map<URL, String> crawledPages;

	public FootballDataManager() {
		this(new HashMap<>());
	}

	public FootballDataManager(Map<URL, String> crawledPages) {
		this.crawledPages = crawledPages;
	}

	/**
	 * MatchMetaData will correctly set the home team and the away one.
	 * This method is using internal crawling so its required network connection and
	 * also keep in mind that its possible to pass teams that are not playing in same match,
	 * but the returned information obviously will be incorrect
	 *
	 * @param match match with team in Bundesliga format and valid year and round
	 * @return match with his metadata in it
	 */
	@Override
	public FootballMatch createData(FootballMatch match) throws Exception {

		String firstTeam = match.getHomeTeam();
		String secondTeam = match.getAwayTeam();
		int year = match.getYear();
		int round = match.getRound();

		TreeMap<String, Object> firstTeamMetaData = getMetaDataForTeam(firstTeam, year, round - 1);
		TreeMap<String, Object> secondTeamMetaData = getMetaDataForTeam(secondTeam, year, round - 1);

		MatchMetaData currentMatchMetaData = new MatchMetaData();
		currentMatchMetaData.setFirstTeamMetaData(firstTeamMetaData);
		currentMatchMetaData.setSecondTeamMetaData(secondTeamMetaData);

		if (firstTeamMetaData.get(VENUE).equals(MatchVenueType.AWAY))
			swapFirstAndSecondTeam(match, currentMatchMetaData);

		// Remove the venue property because we know which one is home and which is away team
		currentMatchMetaData.getFirstTeamMetaData().remove(VENUE);
		currentMatchMetaData.getSecondTeamMetaData().remove(VENUE);

		LOG.info("Successfully created metadata for match {}", match.getSummary());
		return new FootballMatchBuilder(match)
				.setMatchMetaData(currentMatchMetaData)
				.setResult(getMatchResult(firstTeam, year, round, crawledPages))
				.build();
	}

	private TreeMap<String, Object> getMetaDataForTeam(String bundesLigaTeam, int year, int round) throws Exception {

		if (StringUtils.isEmpty(bundesLigaTeam))
			throw new MetaDataCreationException("Team name cannot be empty");

		if (round < MIN_ROUND || round > MAX_ROUND)
			throw new MetaDataCreationException(
					"FootballMatch round " + round + " cannot be less than 2nd one and greater than 34");

		if (year < MIN_YEAR || year > MAX_YEAR)
			throw new MetaDataCreationException(
					"Year '" + year + "' .. should be in range [" + MIN_YEAR + ".." + MAX_YEAR + "]");

		TreeMap<String, Object> currentTeamData = new TreeMap<>();

		currentTeamData.put(POSITION, getTeamRankingPlace(bundesLigaTeam, year, round, crawledPages));
		currentTeamData.put(POINTS, getPoints(bundesLigaTeam, year, round, crawledPages));
		currentTeamData.put(GOAL_DIFFERENCE, getGoalDifference(bundesLigaTeam, year, round, crawledPages));
		currentTeamData.put(VENUE, getMatchVenue(bundesLigaTeam, year, round + 1, crawledPages));
		currentTeamData.put(PREVIOUS_ROUND_STATS, getTeamPerformance(bundesLigaTeam, year, round, crawledPages));
		currentTeamData.put(LAST_FIVE_GAMES, getLastFiveGames(bundesLigaTeam, year, round + 1, crawledPages));

		if (crawledPages.size() > 100)
			clearCache();

		return currentTeamData;
	}

	private void swapFirstAndSecondTeam(FootballMatch match, MatchMetaData currentMatchMetaData) {
		String firstTeam = match.getHomeTeam();
		String secondTeam = match.getAwayTeam();
		match.setHomeTeam(secondTeam);
		match.setAwayTeam(firstTeam);
		TreeMap<String, Object> firstTeamMetData = currentMatchMetaData.getFirstTeamMetaData();
		TreeMap<String, Object> secondTeamMetData = currentMatchMetaData.getSecondTeamMetaData();
		currentMatchMetaData.setFirstTeamMetaData(secondTeamMetData);
		currentMatchMetaData.setSecondTeamMetaData(firstTeamMetData);
	}

	private void clearCache() {
		crawledPages.clear();
	}
}
