package com.bet.manger.commons;

import com.bet.manager.commons.FootballMatch;
import com.bet.manager.commons.IParserHandler;
import com.bet.manager.commons.LiveScoreFootballParserHandler;
import com.bet.manager.commons.Match;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class LiveScoreFootballParserHandlerTest {

    private static IParserHandler handler;
    private static DateFormat format;

    @BeforeClass
    public static void init() {
        handler = new LiveScoreFootballParserHandler();
        format = new SimpleDateFormat("d MMMMM yyyy HH:mm");
    }

    @Test
    public void testValidHtmlParsing() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreFootballParserHandler.class);

        Match expected = new FootballMatch("Tottenham Hotspur", "Sunderland", format.parse("1 January 2016 14:45"));
        Match actual = handler.parse(content, "January 1");

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidHtmlParsing() throws ParseException {
        String content = TestUtils.getResource("livescore-same-teams.txt", LiveScoreFootballParserHandler.class);
        handler.parse(content, "January 1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDate() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreFootballParserHandler.class);
        handler.parse(content, "WTF");
    }
}
