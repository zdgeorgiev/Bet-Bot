package com.bet.manager.core.ai;

import com.bet.manager.models.dao.FootballMatch;

public interface IPredictor<T extends FootballMatch> {

	/**
	 * Method to predict the output from two matches
	 *
	 * @param m first match
	 * @return the name of the winner
	 */
	String predict(T m);
}
