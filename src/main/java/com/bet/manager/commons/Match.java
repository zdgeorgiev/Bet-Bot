package com.bet.manager.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Match {

    private String homeTeamName;
    private String awayTeamName;
    private Date startDate;
    private String result;

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM d YYYY HH:mm");

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
                ", startDate=" + DATE_FORMAT.format(startDate) +
                ", result='" + result + '\'' +
                '}';
    }
}
