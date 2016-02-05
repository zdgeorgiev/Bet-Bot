package com.bet.manager.model;

import com.bet.manager.model.dao.Result;

public class FootballResult extends Result {

	private static final String SCORE_DELIMITER = "-";

	public FootballResult() {
		this.scoreDelimiter = SCORE_DELIMITER;
	}
}
