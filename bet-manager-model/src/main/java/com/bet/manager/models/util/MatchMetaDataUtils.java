package com.bet.manager.models.util;

import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.models.exceptions.InvalidMetaDataConstructorCountException;

public class MatchMetaDataUtils {

	private MatchMetaDataUtils() {

	}

	public static MatchMetaData parse(String matadataString) {

		String[] params = matadataString.split(MatchMetaData.CONSTRUCTOR_PARAMS_SPLITERATOR);

		if (params.length != MatchMetaData.CONSTRUCTOR_PARAMS_COUNT)
			throw new InvalidMetaDataConstructorCountException(
					String.format("Params given count is %s which is not equal to how many the constructor accepts %s",
							params.length, MatchMetaData.CONSTRUCTOR_PARAMS_COUNT));

		MatchMetaData matchMetaData = new MatchMetaData();

		matchMetaData.setHomeTeam(params[0]);
		matchMetaData.setAwayTeam(params[1]);

		matchMetaData.setYear(Integer.parseInt(params[2]));
		matchMetaData.setRound(Integer.parseInt(params[3]));

		// First team metadata
		MatchMetaData.TeamMetaData homeTeamMetaData = new MatchMetaData.TeamMetaData();
		homeTeamMetaData.setPosition(Integer.parseInt(params[4]));
		homeTeamMetaData.setPoints(Integer.parseInt(params[5]));
		homeTeamMetaData.setGoalDifference(Integer.parseInt(params[6]));
		homeTeamMetaData.setVenue(Integer.parseInt(params[7]));

		MatchMetaData.TeamMetaData.PreviousRoundStats homeTeamPreviousRoundStats =
				new MatchMetaData.TeamMetaData.PreviousRoundStats();
		homeTeamPreviousRoundStats.setDistance(Integer.parseInt(params[8]));
		homeTeamPreviousRoundStats.setSprints(Integer.parseInt(params[9]));
		homeTeamPreviousRoundStats.setPasses(Integer.parseInt(params[10]));
		homeTeamPreviousRoundStats.setShots(Integer.parseInt(params[11]));
		homeTeamPreviousRoundStats.setFouls(Integer.parseInt(params[12]));

		homeTeamMetaData.setPreviousRoundStats(homeTeamPreviousRoundStats);

		MatchMetaData.TeamMetaData.LastFiveMatchesPerformance homeTeamLastFiveMatchesPerformance =
				new MatchMetaData.TeamMetaData.LastFiveMatchesPerformance();
		homeTeamLastFiveMatchesPerformance.setHugeWins(Integer.parseInt(params[13]));
		homeTeamLastFiveMatchesPerformance.setHugeLoses(Integer.parseInt(params[14]));
		homeTeamLastFiveMatchesPerformance.setWins(Integer.parseInt(params[15]));
		homeTeamLastFiveMatchesPerformance.setLoses(Integer.parseInt(params[16]));
		homeTeamLastFiveMatchesPerformance.setDraws(Integer.parseInt(params[17]));

		homeTeamMetaData.setLastFiveMatchesPerformance(homeTeamLastFiveMatchesPerformance);

		matchMetaData.setHomeTeamMetaData(homeTeamMetaData);

		// Second team metadata
		MatchMetaData.TeamMetaData awayTeamMetaData = new MatchMetaData.TeamMetaData();
		awayTeamMetaData.setPosition(Integer.parseInt(params[18]));
		awayTeamMetaData.setPoints(Integer.parseInt(params[19]));
		awayTeamMetaData.setGoalDifference(Integer.parseInt(params[20]));
		awayTeamMetaData.setVenue(Integer.parseInt(params[21]));

		MatchMetaData.TeamMetaData.PreviousRoundStats awayTeamPreviousRoundStats =
				new MatchMetaData.TeamMetaData.PreviousRoundStats();
		awayTeamPreviousRoundStats.setDistance(Integer.parseInt(params[22]));
		awayTeamPreviousRoundStats.setSprints(Integer.parseInt(params[23]));
		awayTeamPreviousRoundStats.setPasses(Integer.parseInt(params[24]));
		awayTeamPreviousRoundStats.setShots(Integer.parseInt(params[25]));
		awayTeamPreviousRoundStats.setFouls(Integer.parseInt(params[26]));

		awayTeamMetaData.setPreviousRoundStats(awayTeamPreviousRoundStats);

		MatchMetaData.TeamMetaData.LastFiveMatchesPerformance awayTeamLastFiveMatchesPerformance =
				new MatchMetaData.TeamMetaData.LastFiveMatchesPerformance();
		awayTeamLastFiveMatchesPerformance.setHugeWins(Integer.parseInt(params[27]));
		awayTeamLastFiveMatchesPerformance.setHugeLoses(Integer.parseInt(params[28]));
		awayTeamLastFiveMatchesPerformance.setWins(Integer.parseInt(params[29]));
		awayTeamLastFiveMatchesPerformance.setLoses(Integer.parseInt(params[30]));
		awayTeamLastFiveMatchesPerformance.setDraws(Integer.parseInt(params[31]));

		awayTeamMetaData.setLastFiveMatchesPerformance(awayTeamLastFiveMatchesPerformance);

		matchMetaData.setAwayTeamMetaData(awayTeamMetaData);

		return matchMetaData;
	}
}
