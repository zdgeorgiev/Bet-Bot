package com.bet.manager.commons;

import java.util.Date;

public abstract class Match {

    private String homeTeamName;
    private String awayTeamName;
    private Date startDate;
    private String result;

    public Match(String homeTeamName, String awayTeamName, Date startDate) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.startDate = startDate;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override public String toString() {
        return "Match{" +
                "homeTeamName='" + homeTeamName + '\'' +
                ", awayTeamName='" + awayTeamName + '\'' +
                ", startDate=" + startDate +
                ", result='" + result + '\'' +
                '}';
    }
}
