package com.bet.manger.commons.it;

import com.bet.manager.commons.*;
import com.bet.manger.commons.TestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class LiveScoreMatchCreatorIT {

    private static IMatchParser crawler;
    private static DateFormat format;
    private static int currentYear;

    @BeforeClass
    public static void init() {
        crawler = new LiveScoreMatchParser();
        format = new SimpleDateFormat("d MMMMM yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("EET"));
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Test
    public void testWithFullContent() throws ParseException {
        String content = TestUtils.getResource("live-score-full-content.txt", LiveScoreMatchParser.class);

        List<Match> actual = crawler.parseAll(content);

        List<Match> expected = new ArrayList<Match>() {
            {
                //12 January
                add(new FootballMatch("AFC Bournemouth", "West Ham United",
                        format.parse("12 January " + currentYear + " 21:45")));
                add(new FootballMatch("Aston Villa", "Crystal Palace",
                        format.parse("12 January " + currentYear + " 21:45")));
                add(new FootballMatch("Newcastle United", "Manchester United",
                        format.parse("12 January " + currentYear + " 21:45")));

                //13 January
                add(new FootballMatch("Chelsea", "West Bromwich Albion",
                        format.parse("13 January " + currentYear + " 21:45")));
                add(new FootballMatch("Manchester City", "Everton",
                        format.parse("13 January " + currentYear + " 21:45")));
                add(new FootballMatch("Southampton", "Watford",
                        format.parse("13 January " + currentYear + " 21:45")));
                add(new FootballMatch("Stoke City", "Norwich City",
                        format.parse("13 January " + currentYear + " 21:45")));
                add(new FootballMatch("Swansea City", "Sunderland",
                        format.parse("13 January " + currentYear + " 21:45")));
                add(new FootballMatch("Liverpool", "Arsenal",
                        format.parse("13 January " + currentYear + " 22:00")));
                add(new FootballMatch("Tottenham Hotspur", "Leicester City",
                        format.parse("13 January " + currentYear + " 22:00")));
            }
        };

        Assert.assertTrue(actual.equals(expected));
    }
}