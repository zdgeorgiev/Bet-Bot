package com.bet.manager.core.data;

import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.Espnfc;
import com.bet.manager.core.data.sources.ISecondarySource;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.core.data.sources.exceptions.InvalidMatchRoundIndex;
import com.bet.manager.core.data.sources.exceptions.InvalidMatchYearIndex;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.util.FootballMatchBuilder;
import com.bet.manager.model.util.MatchMetaDataUtils;
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
	 * Method creates {@link FootballMatch} for given two teams, year and round. When will use this method
	 * to get information about future match just give the current year and current round of the league.
	 * FootballMatch which is created also hold its {@link MatchMetaData} information. NOTE that this method
	 * is using internal crawling so its required network connection, also NOTE that its possible to pass teams
	 * that are not playing in same match, but the returned information obviously will be incorrect
	 *
	 * @param firstTeam  firstTeam as Bundesliga name ( NOTE : first team will be correctly parsed as home or away depends on the match)
	 * @param secondTeam secondTeam as Bundesliga name ( NOTE : second team will be correctly parsed as home or away depends on the match)
	 * @param year       match year
	 * @param round      match round should be at least 2nd one, because information for round 0 is invalid
	 * @return Football match for the given year, round
	 */
	public FootballMatch createFootballMatch(String firstTeam, String secondTeam, int year, int round) throws Exception {

		StringBuilder currentMatchData = new StringBuilder();
		String firstTeamMetaData = getMetaDataForTeam(firstTeam, year, round);
		String secondTeamMetaData = getMetaDataForTeam(secondTeam, year, round);

		currentMatchData
				.append(firstTeam).append(DELIMITER)
				.append(secondTeam).append(DELIMITER)
				.append(firstTeamMetaData).append(DELIMITER)
				.append(secondTeamMetaData).append(DELIMITER)
				.append(secondarySource.getMatchResult(firstTeam, year, round, crawledPages));

		MatchMetaData currentMatchMetaData = MatchMetaDataUtils.parse(currentMatchData.toString());

		return new FootballMatchBuilder()
				.setHomeTeamName(currentMatchMetaData.getHomeTeam())
				.setAwayTeamName(currentMatchMetaData.getAwayTeam())
				.setYear(year)
				.setRound(round)
				.setMatchMetaData(currentMatchMetaData)
				.setResult(currentMatchMetaData.getResult())
				.build();
	}

	private String getMetaDataForTeam(String bundesLigaTeam, int year, int round) throws Exception {

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