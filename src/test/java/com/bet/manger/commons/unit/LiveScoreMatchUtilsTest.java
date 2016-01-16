package com.bet.manger.commons.unit;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.FootballMatch;
import com.bet.manager.commons.LiveScoreMatchUtils;
import com.bet.manager.commons.Match;
import com.bet.manger.commons.TestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;

public class LiveScoreMatchUtilsTest {

    @Test
    public void testValidMatchParsingFromHtml() throws ParseException {
        String content = TestUtils.getResource("live-score-match.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Match expected = new FootballMatch("Tottenham Hotspur", "Sunderland",
                DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 1 2016 14:45"));
        Match actual = LiveScoreMatchUtils.create(matchDiv, "January 1");

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testInvalidMatchContentParsing() throws ParseException {
        String content = TestUtils.getResource("live-score-same-teams.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Assert.assertNull(LiveScoreMatchUtils.create(matchDiv, "January 1"));
    }

    @Test
    public void testInvalidDate() throws ParseException {
        String content = TestUtils.getResource("live-score-match.txt", LiveScoreMatchUtils.class);
        Element matchDiv = Jsoup.parse(content);

        Assert.assertNull(LiveScoreMatchUtils.create(matchDiv, "6 January"));
    }
}
