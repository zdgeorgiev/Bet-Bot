package com.bet.manager.services;

import com.bet.manager.model.PredictionsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionInfoService {

	@Autowired
	private FootballMatchService footballMatchService;

	public PredictionsInfo getPredictionsInfo() {
		int correctOnes = footballMatchService.correctPredictedMatchesCount();
		int incorrectOnes = footballMatchService.incorrectPredictedMatchesCount();
		
		return new PredictionsInfo(correctOnes, correctOnes + incorrectOnes);
	}
}
