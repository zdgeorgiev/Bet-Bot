package com.bet.manager.commons;

public abstract class Result {

    /**
     * score in format specified in your sub class
     */
    protected String score;

    /**
     * name of the home team
     */
    protected final String homeTeam;

    /**
     * name of the away team
     */
    protected final String awayTeam;

    public Result(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public abstract String getScore();

    public abstract String getWinner();
}
