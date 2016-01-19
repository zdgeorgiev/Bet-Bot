package com.bet.manager.commons;

public class FootballResult extends Result {

    private static final String SCORE_DELIMITER = "-";

    public FootballResult(String firstTeam, String secondTeam) {
        super(firstTeam, secondTeam, SCORE_DELIMITER);
    }
}
