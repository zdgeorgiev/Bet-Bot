package com.bet.manager.commons;

import org.jsoup.helper.StringUtil;

public class FootballResult extends Result {

    private static final String SCORE_DELIMITER = "-";

    public FootballResult(String firstTeam, String secondTeam, String score) {
        super(firstTeam, secondTeam);
        this.score = score;
    }

    public FootballResult(String homeTeam, String awayTeam) {
        this(homeTeam, awayTeam, "");
    }

    @Override public String getScore() {

        if (StringUtil.isBlank(score)) {
            return ResultMessages.UNKNOWN_RESULT;
        }

        return score;
    }

    @Override
    public String getWinner() {

        if (getScore().equals(ResultMessages.UNKNOWN_RESULT)) {
            return ResultMessages.UNKNOWN_WINNER;
        }

        String[] tokens = this.score.split(FootballResult.SCORE_DELIMITER);
        Integer homeTeamGoals = Integer.parseInt(tokens[0]);
        Integer awayTeamGoals = Integer.parseInt(tokens[1]);

        if (homeTeamGoals > awayTeamGoals) {
            return homeTeam;
        } else if (homeTeamGoals < awayTeamGoals) {
            return awayTeam;
        } else {
            return ResultMessages.TIE_RESULT;
        }
    }
}
