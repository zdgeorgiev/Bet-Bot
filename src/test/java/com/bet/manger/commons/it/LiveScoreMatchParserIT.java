package com.bet.manger.commons.it;

import com.bet.manager.commons.*;
import com.bet.manager.utils.ResourceUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LiveScoreMatchParserIT {

    private static IMatchParser parser;
    private static int currentYear;

    @BeforeClass
    public static void init() {
        parser = new LiveScoreMatchParser();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Test
    public void testWithFullContent() throws ParseException {
        String content = ResourceUtils.getContent("live-score-full-content.txt", LiveScoreMatchParser.class);

        List<Match> actual = parser.parse(content);

        List<Match> expected = new ArrayList<Match>() {
            {
                //12 January
                add(new FootballMatch("AFC Bournemouth", "West Ham United",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 12 " + currentYear + " 21:45")));
                add(new FootballMatch("Aston Villa", "Crystal Palace",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 12 " + currentYear + " 21:45")));
                add(new FootballMatch("Newcastle United", "Manchester United",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 12 " + currentYear + " 21:45")));

                //13 January
                add(new FootballMatch("Chelsea", "West Bromwich Albion",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 21:45")));
                add(new FootballMatch("Manchester City", "Everton",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 21:45")));
                add(new FootballMatch("Southampton", "Watford",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 21:45")));
                add(new FootballMatch("Stoke City", "Norwich City",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 21:45")));
                add(new FootballMatch("Swansea City", "Sunderland",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 21:45")));
                add(new FootballMatch("Liverpool", "Arsenal",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 22:00")));
                add(new FootballMatch("Tottenham Hotspur", "Leicester City",
                        DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 13 " + currentYear + " 22:00")));
            }
        };

        Assert.assertTrue(actual.equals(expected));
    }
}