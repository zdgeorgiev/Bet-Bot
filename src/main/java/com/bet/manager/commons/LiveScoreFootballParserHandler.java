package com.bet.manager.commons;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LiveScoreFootballParserHandler implements IParserHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LiveScoreFootballParserHandler.class);

    private static final Calendar cal = Calendar.getInstance();

    private static final String HOME_TEAM_DIV_SELECTOR = "div.ply.tright.name";
    private static final String AWAY_TEAM_DIV_SELECTOR = "div.ply.name";
    private static final String START_TIME_DIV_SELECTOR = "div.min";

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM d yyyy HH:mm");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GTM"));
    }

    @Override
    public Match parse(String matchHtml, String startingDate) {

        if (StringUtil.isBlank(matchHtml)) {
            throw new IllegalArgumentException("Html containing the match cannot be null or empty");
        }

        if (StringUtil.isBlank(startingDate)) {
            throw new IllegalArgumentException("Starting date cannot be null or empty");
        }

        Element doc = Jsoup.parse(matchHtml);

        String homeTeam = doc.select(HOME_TEAM_DIV_SELECTOR).first().text();
        String awayTeam = doc.select(AWAY_TEAM_DIV_SELECTOR).first().text();
        String startTime = doc.select(START_TIME_DIV_SELECTOR).first().text();

        if (StringUtil.isBlank(homeTeam)) {
            throw new IllegalArgumentException("Home team name cannot be null or empty");
        }

        if (StringUtil.isBlank(awayTeam)) {
            throw new IllegalArgumentException("Away team name cannot be null or empty");
        }

        if (StringUtil.isBlank(startTime)) {
            throw new IllegalArgumentException("Start time cannot be null or empty");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalStateException("Home team '" + homeTeam + "' cannot be the same as away team");
        }

        Date finalDate;
        String finalDateString = "";
        try {
            finalDateString = String.format("%s %s %s", startingDate, cal.get(Calendar.YEAR), startTime);
            finalDate = DATE_FORMAT.parse(finalDateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date - '" + finalDateString + "'");
        }

        Match m = new FootballMatch(homeTeam, awayTeam, finalDate);
        LOG.info("Successfully parsed - {}", m);
        return m;
    }
}
