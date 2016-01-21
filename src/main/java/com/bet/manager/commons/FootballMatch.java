package com.bet.manager.commons;

import java.util.Date;

public class FootballMatch extends Match {

	public FootballMatch(String homeTeamName, String awayTeamName, Date startDate) {
		super(homeTeamName, awayTeamName, startDate, new FootballResult(homeTeamName, awayTeamName));
	}
}
