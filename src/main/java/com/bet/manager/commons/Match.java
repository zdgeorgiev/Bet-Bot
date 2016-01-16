package com.bet.manager.commons;

import java.util.Date;

public abstract class Match {

    private String homeTeamName;
    private String awayTeamName;
    private Date startDate;
    private Result result;

    public Match(String homeTeamName, String awayTeamName, Date startDate, Result result) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.startDate = startDate;
        this.result = result;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getResult() {
        return result.getScore();
    }

    public String getWinner() {
        return this.result.getWinner();
    }

    public void setResult(String result) {
        this.result.setScore(result);
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override public String toString() {
        return String
                .format("Match { [%s] %s %s - %s }",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.format(getStartDate()),
                        getResult(),
                        getHomeTeamName(),
                        getAwayTeamName());
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Match match = (Match) o;

        if (!homeTeamName.equals(match.homeTeamName))
            return false;
        if (!awayTeamName.equals(match.awayTeamName))
            return false;
        return startDate.equals(match.startDate);

    }

    @Override public int hashCode() {
        int result = homeTeamName.hashCode();
        result = 31 * result + awayTeamName.hashCode();
        result = 31 * result + startDate.hashCode();
        return result;
    }
}
