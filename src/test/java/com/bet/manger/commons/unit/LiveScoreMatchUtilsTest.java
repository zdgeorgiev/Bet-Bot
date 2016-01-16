package com.bet.manger.commons.unit;

import com.bet.manager.commons.FootballMatch;
import com.bet.manager.commons.LiveScoreMatchUtils;
import com.bet.manager.commons.Match;
import com.bet.manger.commons.TestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LiveScoreMatchUtilsTest {

    private static LiveScoreMatchUtils parser;
    private static DateFormat format;

    @BeforeClass
    public static void init() {
        parser = new LiveScoreMatchUtils();
        format = new SimpleDateFormat("d MMMMM yyyy HH:mm");
    }

    @Test
    public void testValidMatchParsingFromHtml() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Match expected = new FootballMatch("Tottenham Hotspur", "Sunderland", format.parse("1 January 2016 14:45"));
        Match actual = parser.create(matchDiv, "January 1");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidMatchContentParsing() throws ParseException {
        String content = TestUtils.getResource("livescore-same-teams.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Assert.assertNull(parser.create(matchDiv, "January 1"));
    }

    @Test
    public void testInvalidDate() throws ParseException {
        String content = TestUtils.getResource("livescore-match.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Assert.assertNull(parser.create(matchDiv, "WTF"));
    }
}
