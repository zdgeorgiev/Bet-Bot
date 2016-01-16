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

    /**
     * delimiter that is used in the score
     * example: 2-2 (delimiter is - )
     */
    protected final String delimiter;

    public Result(String homeTeam, String awayTeam, String delimiter) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.delimiter = delimiter;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public abstract String getScore();

    public abstract String getWinner();
}
