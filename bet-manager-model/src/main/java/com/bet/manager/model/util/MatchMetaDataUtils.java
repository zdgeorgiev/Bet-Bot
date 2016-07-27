package com.bet.manager.model.util;

import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.exceptions.InvalidMetaDataConstructorCountException;

public class MatchMetaDataUtils {

	private MatchMetaDataUtils() {

	}

	public static MatchMetaData parse(String matadataString) {

		String[] params = matadataString.split(MatchMetaData.CONSTRUCTOR_PARAMS_DELIMITER);

		if (params.length != MatchMetaData.CONSTRUCTOR_PARAMS_COUNT)
			throw new InvalidMetaDataConstructorCountException(
					String.format("Params given count is %s which is not equal to how many the constructor accepts %s",
							params.length, MatchMetaData.CONSTRUCTOR_PARAMS_COUNT));

		MatchMetaData matchMetaData = new MatchMetaData();

		// First team metadata
		MatchMetaData.TeamMetaData firstTeamMetaData = new MatchMetaData.TeamMetaData();
		firstTeamMetaData.setPosition(Integer.parseInt(params[2]));
		firstTeamMetaData.setPoints(Integer.parseInt(params[3]));
		firstTeamMetaData.setGoalDifference(Integer.parseInt(params[4]));
		firstTeamMetaData.setVenue(Integer.parseInt(params[5]));

		MatchMetaData.TeamMetaData.PreviousRoundStats firstTeamPreviousRoundStats =
				new MatchMetaData.TeamMetaData.PreviousRoundStats();
		firstTeamPreviousRoundStats.setDistance(Integer.parseInt(params[6]));
		firstTeamPreviousRoundStats.setSprints(Integer.parseInt(params[7]));
		firstTeamPreviousRoundStats.setPasses(Integer.parseInt(params[8]));
		firstTeamPreviousRoundStats.setShots(Integer.parseInt(params[9]));
		firstTeamPreviousRoundStats.setFouls(Integer.parseInt(params[10]));

		firstTeamMetaData.setPreviousRoundStats(firstTeamPreviousRoundStats);

		MatchMetaData.TeamMetaData.LastFiveMatchesPerformance firstTeamLastFiveMatchesPerformance =
				new MatchMetaData.TeamMetaData.LastFiveMatchesPerformance();
		firstTeamLastFiveMatchesPerformance.setHugeWins(Integer.parseInt(params[11]));
		firstTeamLastFiveMatchesPerformance.setHugeLoses(Integer.parseInt(params[12]));
		firstTeamLastFiveMatchesPerformance.setWins(Integer.parseInt(params[13]));
		firstTeamLastFiveMatchesPerformance.setLoses(Integer.parseInt(params[14]));
		firstTeamLastFiveMatchesPerformance.setDraws(Integer.parseInt(params[15]));

		firstTeamMetaData.setLastFiveMatchesPerformance(firstTeamLastFiveMatchesPerformance);

		// Second team metadata
		MatchMetaData.TeamMetaData secondTeamMetaData = new MatchMetaData.TeamMetaData();
		secondTeamMetaData.setPosition(Integer.parseInt(params[16]));
		secondTeamMetaData.setPoints(Integer.parseInt(params[17]));
		secondTeamMetaData.setGoalDifference(Integer.parseInt(params[18]));
		secondTeamMetaData.setVenue(Integer.parseInt(params[19]));

		MatchMetaData.TeamMetaData.PreviousRoundStats secondTeamPreviousRoundStats =
				new MatchMetaData.TeamMetaData.PreviousRoundStats();
		secondTeamPreviousRoundStats.setDistance(Integer.parseInt(params[20]));
		secondTeamPreviousRoundStats.setSprints(Integer.parseInt(params[21]));
		secondTeamPreviousRoundStats.setPasses(Integer.parseInt(params[22]));
		secondTeamPreviousRoundStats.setShots(Integer.parseInt(params[23]));
		secondTeamPreviousRoundStats.setFouls(Integer.parseInt(params[24]));

		secondTeamMetaData.setPreviousRoundStats(secondTeamPreviousRoundStats);

		MatchMetaData.TeamMetaData.LastFiveMatchesPerformance secondTeamLastFiveMatchesPerformance =
				new MatchMetaData.TeamMetaData.LastFiveMatchesPerformance();
		secondTeamLastFiveMatchesPerformance.setHugeWins(Integer.parseInt(params[25]));
		secondTeamLastFiveMatchesPerformance.setHugeLoses(Integer.parseInt(params[26]));
		secondTeamLastFiveMatchesPerformance.setWins(Integer.parseInt(params[27]));
		secondTeamLastFiveMatchesPerformance.setLoses(Integer.parseInt(params[28]));
		secondTeamLastFiveMatchesPerformance.setDraws(Integer.parseInt(params[29]));

		secondTeamMetaData.setLastFiveMatchesPerformance(secondTeamLastFiveMatchesPerformance);

		matchMetaData.setResult(params[30]);

		boolean teamsRightOrder = true;

		// Should do swap if the first team is away
		if (firstTeamMetaData.getVenue() == -1) {
			teamsRightOrder = false;
		}

		matchMetaData.setHomeTeam(teamsRightOrder ? params[0] : params[1]);
		matchMetaData.setAwayTeam(teamsRightOrder ? params[1] : params[0]);

		matchMetaData.setHomeTeamMetaData(teamsRightOrder ? firstTeamMetaData : secondTeamMetaData);
		matchMetaData.setAwayTeamMetaData(teamsRightOrder ? secondTeamMetaData : firstTeamMetaData);

		return matchMetaData;
	}
}
