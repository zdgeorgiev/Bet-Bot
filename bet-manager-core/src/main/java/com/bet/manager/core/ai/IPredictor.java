package com.bet.manager.core.ai;

public interface IPredictor<FootballMatch> {

	/**
	 * Method to predict the output from two matches
	 *
	 * @param m first match
	 * @return the name of the winner
	 */
	String predict(FootballMatch m);
}
