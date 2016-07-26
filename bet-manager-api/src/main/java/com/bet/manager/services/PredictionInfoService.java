package com.bet.manager.services;

import com.bet.manager.model.PredictionsInfo;
import com.bet.manager.model.repository.FootballMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PredictionInfoService {

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	public PredictionsInfo getPredictionsInfo() {
		return null;
	}
}
