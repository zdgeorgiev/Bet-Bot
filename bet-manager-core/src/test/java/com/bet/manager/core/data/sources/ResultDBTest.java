package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.ClasspathUtils;
import org.junit.Assert;
import org.junit.Test;

public class ResultDBTest {

    @Test
    public void testCorrectGettingTheFirstRoundOpponent() {

        String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
        String[] actual = ResultDB.parseTeamOpponentAndVenue(content, 2);
        String[] expected = new String[]{"Borussia M'gladbach", "-1"};
        Assert.assertArrayEquals(actual, expected);
    }

    @Test
    public void testCorrectGettingThirdRoundOpponent() {

        String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
        String[] actual = ResultDB.parseTeamOpponentAndVenue(content, 3);
        String[] expected = new String[]{"Bayer 04 Leverkusen", "1"};
        Assert.assertArrayEquals(actual, expected);
    }

    @Test
    public void testCorrectGettingLastFiveMatches() {

        String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
        String actual = ResultDB.parseLastFiveGamesForTeam(content, 6);
        String expected = "2 0 0 2 1";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testCorrectGettingFiveLastMatchesButContainsOnlyThree() {

        String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
        String actual = ResultDB.parseLastFiveGamesForTeam(content, 4);
        String expected = "1 0 0 1 1";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testCorrectGettingFiveLastMatchesForRound34() {

        String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
        String actual = ResultDB.parseLastFiveGamesForTeam(content, 34);
        String expected = "3 1 0 0 1";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testCorrectParsingResultToNormalizationArrayForWin() {

        int[] normalizationArray = new int[5];
        String result = "W";
        String score = "1-0";
        ResultDB.addMatchToNormalizationArray(result, score, normalizationArray);
        Assert.assertEquals(normalizationArray[2], 1);
    }

    @Test
    public void testCorrectParsingResultToNormalizationArrayForTie() {

        int[] normalizationArray = new int[5];
        String result = "D";
        String score = "1-1";
        ResultDB.addMatchToNormalizationArray(result, score, normalizationArray);
        Assert.assertEquals(normalizationArray[4], 1);
    }

    @Test
    public void testCorrectParsingResultToNormalizationArrayForHugeWin() {

        int[] normalizationArray = new int[5];
        String result = "W";
        String score = "3-0";
        ResultDB.addMatchToNormalizationArray(result, score, normalizationArray);
        Assert.assertEquals(normalizationArray[0], 1);
    }

    @Test
    public void testCorrectParsingTwoResultToNormalizationArrayForHugeWin() {

        int[] normalizationArray = new int[5];
        String result = "W";
        String score = "3-0";
        ResultDB.addMatchToNormalizationArray(result, score, normalizationArray);
        ResultDB.addMatchToNormalizationArray(result, score, normalizationArray);
        Assert.assertEquals(normalizationArray[0], 2);
    }

    @Test
    public void testCorrectParsingTheResultForGivenMatch() {
        Assert.assertTrue(false);
    }
}
