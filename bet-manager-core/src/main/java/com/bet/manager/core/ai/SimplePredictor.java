package com.bet.manager.core.ai;

import com.bet.manager.models.dao.FootballMatchWithPrediction;

import java.util.Random;

public class SimplePredictor implements IPredictor<FootballMatchWithPrediction> {

	@Override
	public String predict(FootballMatchWithPrediction m) {

		return new Random().nextInt(2) == 0 ? m.getHomeTeam() : m.getAwayTeam();
	}
}
