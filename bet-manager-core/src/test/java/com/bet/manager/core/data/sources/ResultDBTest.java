package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.exceptions.MatchResultNotFound;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultDBTest {

	private static final String HUGE_WINS = "hugeWins";
	private static final String HUGE_LOSES = "hugeLoses";
	private static final String WINS = "wins";
	private static final String LOSES = "loses";
	private static final String DRAWS = "draws";

	@Test
	public void testCorrectGettingTheFirstRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = ResultDB.parseTeamOpponent(content, 2);
		String expected = "Borussia M'gladbach";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingTheFirstRoundVenue() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = ResultDB.parseMatchVenue(content, 2);
		Assert.assertEquals("-1", actual);
	}

	@Test
	public void testCorrectGettingThirdRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = ResultDB.parseTeamOpponent(content, 3);
		String expected = "Bayer 04 Leverkusen";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingThirdRoundVenue() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = ResultDB.parseMatchVenue(content, 3);
		Assert.assertEquals("1", actual);
	}

	@Test
	public void testCorrectGettingLastFiveMatches() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		Map<String, Integer> actual = ResultDB.parseLastFiveGamesForTeam(content, 6);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 2);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 0);
		expected.put(LOSES, 2);
		expected.put(DRAWS, 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingFiveLastMatchesButContainsOnlyThree() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		Map<String, Integer> actual = ResultDB.parseLastFiveGamesForTeam(content, 4);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 1);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 0);
		expected.put(LOSES, 1);
		expected.put(DRAWS, 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingFiveLastMatchesForRound34() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		Map<String, Integer> actual = ResultDB.parseLastFiveGamesForTeam(content, 34);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 3);
		expected.put(HUGE_LOSES, 1);
		expected.put(WINS, 0);
		expected.put(LOSES, 0);
		expected.put(DRAWS, 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForWin() {

		Map<String, Integer> actual = new LinkedHashMap<>();
		actual.put(HUGE_WINS, 0);
		actual.put(HUGE_LOSES, 0);
		actual.put(WINS, 0);
		actual.put(LOSES, 0);
		actual.put(DRAWS, 0);

		String result = "W";
		String score = "1-0";
		ResultDB.addToHistogram(result, score, actual);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 0);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 1);
		expected.put(LOSES, 0);
		expected.put(DRAWS, 0);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForTie() {

		Map<String, Integer> actual = new LinkedHashMap<>();
		actual.put(HUGE_WINS, 0);
		actual.put(HUGE_LOSES, 0);
		actual.put(WINS, 0);
		actual.put(LOSES, 0);
		actual.put(DRAWS, 0);

		String result = "D";
		String score = "1-1";
		ResultDB.addToHistogram(result, score, actual);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 0);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 0);
		expected.put(LOSES, 0);
		expected.put(DRAWS, 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForHugeWin() {

		Map<String, Integer> actual = new LinkedHashMap<>();
		actual.put(HUGE_WINS, 0);
		actual.put(HUGE_LOSES, 0);
		actual.put(WINS, 0);
		actual.put(LOSES, 0);
		actual.put(DRAWS, 0);

		String result = "W";
		String score = "3-0";
		ResultDB.addToHistogram(result, score, actual);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 1);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 0);
		expected.put(LOSES, 0);
		expected.put(DRAWS, 0);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingTwoResultToNormalizationArrayForHugeWin() {

		Map<String, Integer> actual = new LinkedHashMap<>();
		actual.put(HUGE_WINS, 0);
		actual.put(HUGE_LOSES, 0);
		actual.put(WINS, 0);
		actual.put(LOSES, 0);
		actual.put(DRAWS, 0);

		String result = "W";
		String score = "3-0";
		ResultDB.addToHistogram(result, score, actual);
		ResultDB.addToHistogram(result, score, actual);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put(HUGE_WINS, 2);
		expected.put(HUGE_LOSES, 0);
		expected.put(WINS, 0);
		expected.put(LOSES, 0);
		expected.put(DRAWS, 0);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingTheResultForFirstMatch() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		String result = ResultDB.parseMatchResult(1, content);
		Assert.assertEquals("3-0", result);
	}

	@Test
	public void testCorrectParsingTheResultForLastRound() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		String result = ResultDB.parseMatchResult(34, content);
		Assert.assertEquals("3-2", result);
	}

	@Test(expected = MatchResultNotFound.class)
	public void testCorrectParsingTheResultFor35RoundShouldThrowException() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		ResultDB.parseMatchResult(35, content);
	}

	@Test(expected = MatchResultNotFound.class)
	public void testCorrectParsingTheResultFor0RoundShouldThrowException() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		ResultDB.parseMatchResult(0, content);
	}
}
