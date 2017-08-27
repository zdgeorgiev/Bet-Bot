package com.bet.manager.services;

import com.bet.manager.model.PredictionsInfo;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import com.bet.manager.model.repository.FootballMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionInfoService {

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	public int correctPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndMatchStatus(PredictionType.CORRECT, MatchStatus.FINISHED).size();
	}

	public int incorrectPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndMatchStatus(PredictionType.INCORRECT, MatchStatus.FINISHED).size();
	}

	public PredictionsInfo getPredictionsInfo() {
		int correctOnes = correctPredictedMatchesCount();
		int incorrectOnes = incorrectPredictedMatchesCount();

		return new PredictionsInfo(correctOnes, correctOnes + incorrectOnes);
	}
}
