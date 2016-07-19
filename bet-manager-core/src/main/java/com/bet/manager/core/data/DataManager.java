package com.bet.manager.core.data;

import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.Espnfc;
import com.bet.manager.core.data.sources.ISecondarySource;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.core.data.sources.exceptions.InvalidMatchRoundIndex;
import com.bet.manager.core.data.sources.exceptions.InvalidMatchYearIndex;
import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.models.util.MatchMetaDataUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

	private static final int MIN_ROUND = 2;
	private static final int MAX_ROUND = 34;
	private static final int MIN_YEAR = 2011;
	private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);

	private static final String DELIMITER = MatchMetaData.CONSTRUCTOR_PARAMS_DELIMITER;

	private Map<URL, String> crawledPages;
	private ISecondarySource secondarySource;

	/**
	 * Const which gets a memorization map for crawled pages.
	 * This constructor is useful when the data manager is using external memorization map
	 * This will dynamic create entry for incoming matches. Check other constructors.
	 */
	public DataManager() {
		this(true, new HashMap<>());
	}

	/**
	 * Const which gets a memorization map for crawled pages.
	 * This constructor is useful when the data manager is using external memorization map
	 *
	 * @param dynamicMatches If this is set to false DataManager will try to get information from
	 *                       ResultDB source. This is not appropriate if you are looking for dynamic
	 *                       match information, because this source publish all the data after the football
	 *                       season. This should be used only for getting information about past season matches.
	 */
	public DataManager(boolean dynamicMatches) {
		this(dynamicMatches, new HashMap<>());
	}

	/**
	 * Const which gets a memorization map for crawled pages.
	 * This constructor is useful when the data manager is using external memorization map
	 *
	 * @param dynamicMatches If this is set to false DataManager will try to get information from
	 *                       ResultDB source. This is not appropriate if you are looking for dynamic
	 *                       match information, because this source publish all the data after the football
	 *                       season. This should be used only for getting information about past season matches.
	 * @param crawledPages   memorization map for crawled pages
	 */
	public DataManager(boolean dynamicMatches, Map<URL, String> crawledPages) {

		if (dynamicMatches)
			secondarySource = new Espnfc();
		else
			secondarySource = new ResultDB();

		this.crawledPages = crawledPages;
	}

	/**
	 * Method which creates meta data for given home, away team, year and round. These method should be used
	 * while want to get information about future match, just passes the current year and expected round.
	 * The data is in the format : NOTE! Between all parameters is using MatchMetaData.DELIMITER
	 * [round] (for both teams - [ladderPosition] [currentRoundStats] [venue] [prevRoundStats] [lastFiveGames])
	 * <p/>
	 * [ladderPosition] - Current position in the ranking table for the round.
	 * [currentRoundStats] - Current points and round goals difference.
	 * [venue] - Home/Away
	 * [prevRoundStats] - [track distance] [sprints] [passes] [shots] [fouls].
	 * [lastFiveGames] - Consist of 5 type of game end. Huge win [HW], Huge Lose [HL], Win [W], Lose [W], Tie [T],
	 * home team goals -  away team goals >= 2 - HW, <= 2 - HL, else W,L,T.
	 * <p/>
	 *
	 * @param homeTeam homeTeam as Bundesliga name
	 * @param awayTeam awayTeam as Bundesliga name
	 * @param year     match year
	 * @param round    match round should be at least 2nd one, because information for round 0 is invalid
	 * @return Match Meta Data for the match
	 */
	public MatchMetaData getDataForMatch(String homeTeam, String awayTeam, int year, int round) throws Exception {

		StringBuilder currentMatchData = new StringBuilder();
		String homeTeamData = getDataForTeam(homeTeam, year, round);
		String awayTeamData = getDataForTeam(awayTeam, year, round);

		currentMatchData
				.append(homeTeam).append(DELIMITER)
				.append(awayTeam).append(DELIMITER)
				.append(year).append(DELIMITER)
				.append(round).append(DELIMITER)
				.append(homeTeamData).append(DELIMITER)
				.append(awayTeamData).append(DELIMITER)
				.append(secondarySource.getMatchResult(homeTeam, year, round, crawledPages));

		return MatchMetaDataUtils.parse(currentMatchData.toString());
	}

	private String getDataForTeam(String bundesLigaTeam, int year, int round) throws Exception {

		if (StringUtils.isEmpty(bundesLigaTeam))
			throw new IllegalArgumentException("Team argument cannot be empty");

		if (round < MIN_ROUND || round > MAX_ROUND)
			throw new InvalidMatchRoundIndex("FootballMatch round " + round + " cannot be less than 2nd one and greater than 34");

		if (year < MIN_YEAR || year > MAX_YEAR)
			throw new InvalidMatchYearIndex("Year '" + year + "' .. should be in range [" + MIN_YEAR + ".." + MAX_YEAR + "]");

		StringBuilder currentTeamData = new StringBuilder();

		currentTeamData
				.append(Bundesliga.getTeamRankingPlace(bundesLigaTeam, year, round, crawledPages)).append(DELIMITER)
				.append(Bundesliga.getCurrentRankingStats(bundesLigaTeam, year, round, crawledPages)).append(DELIMITER)
				.append(secondarySource.getMatchVenue(bundesLigaTeam, year, round, crawledPages)).append(DELIMITER)
				.append(Bundesliga.getTeamPerformance(bundesLigaTeam, year, round - 1, crawledPages)).append(DELIMITER)
				.append(secondarySource.getLastFiveGamesForTeam(bundesLigaTeam, year, round, crawledPages));

		if (crawledPages.size() > 100)
			clearCache();

		return currentTeamData.toString();
	}

	private void clearCache() {
		crawledPages.clear();
	}
}