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
    private static final String AWAY_TEAM_SELECTOR = "div.ply.name";
    private static final String START_TIME_SELECTOR = "div.min";

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM d yyyy HH:mm");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GTM"));
    }

    private static final Calendar cal = Calendar.getInstance();

    @Override
    public Match parse(String matchAsHtmlString, String startingDate) {

        Element doc = Jsoup.parse(matchAsHtmlString);

        String homeTeam = doc.select(HOME_TEAM_DIV_SELECTOR).first().text();
        String awayTeam = doc.select(AWAY_TEAM_SELECTOR).first().text();
        String startTime = doc.select(START_TIME_SELECTOR).first().text();

        String[] timeTokens = startTime.split(":");
        Integer hours = Integer.parseInt(timeTokens[0]);
        Integer minutes = Integer.parseInt(timeTokens[1]);

        Date finalDate;
        String finalDateString = "";
        try {
            finalDateString = startingDate + " " + cal.get(Calendar.YEAR) + " " + hours + ":" + minutes;
            finalDate = DATE_FORMAT.parse(finalDateString);
        } catch (ParseException e) {
            throw new IllegalStateException("Cannot parse date - '" + finalDateString + "'");
        }

        return new FootballMatch(homeTeam, awayTeam, finalDate);
    }
}
