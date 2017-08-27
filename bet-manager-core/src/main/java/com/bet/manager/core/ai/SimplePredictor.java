package com.bet.manager.core.ai;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.entity.FootballMatch;

import java.util.Random;

public class SimplePredictor implements IPredictor {

	@Override
	public String predict(FootballMatch m) {

		int random = new Random().nextInt(3);

		switch (random) {
		case 0:
			return ResultMessages.TIE_RESULT;
		case 1:
			return m.getHomeTeam();
		case 2:
			return m.getAwayTeam();
		}

		throw new RuntimeException();
	}
}
