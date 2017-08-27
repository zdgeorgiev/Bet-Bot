package com.bet.manager.core.ai;

import com.bet.manager.model.entity.FootballMatch;

public interface IPredictor {

	/**
	 * Method to predict the output from two matches
	 *
	 * @param m first match
	 * @return the name of the winner
	 */
	String predict(FootballMatch m);
}
