package com.bet.manager.core.ai;

import com.bet.manager.models.dao.Match;

public interface IPredictor<T extends Match> {

	/**
	 * Method to predict the output from two matches
	 *
	 * @param m first match
	 * @return the name of the winner
	 */
	String predict(T m);
}
