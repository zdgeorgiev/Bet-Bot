package com.bet.manager.services;

import com.bet.manager.exceptions.NoMatchesInTheDataBaseExceptions;
import com.bet.manager.model.PredictionsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionInfoService {

	@Autowired
	private FootballMatchService footballMatchService;

	public PredictionsInfo getPredictionsInfo() {
		int totalMatches = footballMatchService.matchesCount();
		int correctOnes = footballMatchService.correctPredictedMatchesCount();

		if (totalMatches == 0)
			throw new NoMatchesInTheDataBaseExceptions("There is no matches in the DB");

		return new PredictionsInfo(correctOnes, totalMatches);
	}
}
