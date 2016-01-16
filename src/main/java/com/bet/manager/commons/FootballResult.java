package com.bet.manager.commons;

import org.jsoup.helper.StringUtil;

public class FootballResult extends Result {

    private static final String SCORE_DELIMITER = "-";

    public FootballResult(String firstTeam, String secondTeam) {
        super(firstTeam, secondTeam, SCORE_DELIMITER);
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

        String[] tokens = this.score.split(this.delimiter);
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
