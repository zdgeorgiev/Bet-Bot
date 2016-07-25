package com.bet.manager.core.ai;

import com.bet.manager.model.dao.FootballMatch;

import java.util.Random;

public class SimplePredictor implements IPredictor<FootballMatch> {

	@Override
	public String predict(FootballMatch m) {

		return new Random().nextInt(2) == 0 ? m.getHomeTeam() : m.getAwayTeam();
	}
}
