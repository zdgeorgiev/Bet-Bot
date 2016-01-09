package com.bet.manager.commons;

import org.slf4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveScoreFootballParserHandler implements IParserHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LiveScoreFootballParserHandler.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy at hh:mm");

    private static final String HOME_TEAM_DIV_SELECTOR = "div.ply tright name";
    private static final String AWAY_TEAM_SELECTOR = "div.ply name";
    private static final String START_TIME_SELECTOR = "div.min";

    @Override
    public Match parse(String matchAsHtmlString, Date startingDate) {

        Element doc = Jsoup.parse(matchAsHtmlString);

        String homeTeam = doc.select(HOME_TEAM_DIV_SELECTOR).first().text();
        String awayTeam = doc.select(AWAY_TEAM_SELECTOR).first().text();
        String startTime = doc.select(START_TIME_SELECTOR).first().text();

        String startDateString = startingDate.toString() + startTime;

        Date exatclyStartingDate = null;
        try {
            exatclyStartingDate = DATE_FORMAT.parse(startDateString);
        } catch (ParseException e) {
            LOG.error("Cannot parse date {}", startDateString);
            e.printStackTrace();
        }

        return new FootballMatch(homeTeam, awayTeam, exatclyStartingDate);
    }
}
