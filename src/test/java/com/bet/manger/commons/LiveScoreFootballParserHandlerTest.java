package com.bet.manger.commons;

import com.bet.manager.commons.FootballMatch;
import com.bet.manager.commons.LiveScoreFootballParser;
import com.bet.manager.commons.Match;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LiveScoreFootballParserHandlerTest {

    private static LiveScoreFootballParser parser;
    private static DateFormat format;

    @BeforeClass
    public static void init() {
        parser = new LiveScoreFootballParser();
        format = new SimpleDateFormat("d MMMMM yyyy HH:mm");
    }

    @Test
    public void testValidHtmlParsing() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreFootballParser.class);

        Match expected = new FootballMatch("Tottenham Hotspur", "Sunderland", format.parse("1 January 2016 14:45"));
        Match actual = parser.parse(content, "January 1");

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidHtmlParsing() throws ParseException {
        String content = TestUtils.getResource("livescore-same-teams.txt", LiveScoreFootballParser.class);
        parser.parse(content, "January 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreFootballParser.class);
        parser.parse(content, "WTF");
    }
}
