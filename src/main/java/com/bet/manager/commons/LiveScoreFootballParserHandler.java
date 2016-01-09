package com.bet.manager.commons;

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

    private static final String HOME_TEAM_DIV_SELECTOR = "div.ply.tright.name";
    private static final String AWAY_TEAM_DIV_SELECTOR = "div.ply.name";
    private static final String START_TIME_DIV_SELECTOR = "div.min";

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM d yyyy HH:mm");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GTM"));
    }

    private static final Calendar cal = Calendar.getInstance();

    @Override
    public Match parse(String matchHtml, String startingDate) {

        Element doc = Jsoup.parse(matchHtml);

        String homeTeam = doc.select(HOME_TEAM_DIV_SELECTOR).first().text();
        String awayTeam = doc.select(AWAY_TEAM_DIV_SELECTOR).first().text();
        String startTime = doc.select(START_TIME_DIV_SELECTOR).first().text();

        Date finalDate;
        String finalDateString = "";
        try {
            finalDateString = String.format("%s %s %s", startingDate, cal.get(Calendar.YEAR), startTime);
            finalDate = DATE_FORMAT.parse(finalDateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date - '" + finalDateString + "'");
        }

        return new FootballMatch(homeTeam, awayTeam, finalDate);
    }
}
