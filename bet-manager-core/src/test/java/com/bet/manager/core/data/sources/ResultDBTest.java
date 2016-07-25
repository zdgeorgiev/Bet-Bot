package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.data.sources.exceptions.MatchResultNotFound;
import com.bet.manager.model.dao.MatchMetaData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResultDBTest {

	private static final String DELIMITER = MatchMetaData.CONSTRUCTOR_PARAMS_DELIMITER;

	ISecondarySource source = new ResultDB();

	@Test
	public void testCorrectGettingTheFirstRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseTeamOpponent(content, 2);
		String expected = "Borussia M'gladbach";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingTheFirstRoundVenue() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseMatchVenue(content, 2);
		String expected = "-1";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingThirdRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseTeamOpponent(content, 3);
		String expected = "Bayer 04 Leverkusen";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingThirdRoundVenue() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseMatchVenue(content, 3);
		String expected = "1";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingLastFiveMatches() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseLastFiveGamesForTeam(content, 6);

		List<String> performance = Arrays.asList("2", "0", "0", "2", "1");
		String expected = performance.stream()
				.collect(Collectors.joining(DELIMITER));

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingFiveLastMatchesButContainsOnlyThree() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseLastFiveGamesForTeam(content, 4);

		List<String> performance = Arrays.asList("1", "0", "0", "1", "1");
		String expected = performance.stream()
				.collect(Collectors.joining(DELIMITER));

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectGettingFiveLastMatchesForRound34() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String actual = source.parseLastFiveGamesForTeam(content, 34);

		List<String> performance = Arrays.asList("3", "1", "0", "0", "1");
		String expected = performance.stream()
				.collect(Collectors.joining(DELIMITER));

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForWin() {

		int[] normalizationArray = new int[5];
		String result = "W";
		String score = "1-0";
		source.addMatchToNormalizationArray(result, score, normalizationArray);
		Assert.assertEquals(1, normalizationArray[2]);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForTie() {

		int[] normalizationArray = new int[5];
		String result = "D";
		String score = "1-1";
		source.addMatchToNormalizationArray(result, score, normalizationArray);
		Assert.assertEquals(1, normalizationArray[4]);
	}

	@Test
	public void testCorrectParsingResultToNormalizationArrayForHugeWin() {

		int[] normalizationArray = new int[5];
		String result = "W";
		String score = "3-0";
		source.addMatchToNormalizationArray(result, score, normalizationArray);
		Assert.assertEquals(1, normalizationArray[0]);
	}

	@Test
	public void testCorrectParsingTwoResultToNormalizationArrayForHugeWin() {

		int[] normalizationArray = new int[5];
		String result = "W";
		String score = "3-0";
		source.addMatchToNormalizationArray(result, score, normalizationArray);
		source.addMatchToNormalizationArray(result, score, normalizationArray);
		Assert.assertEquals(2, normalizationArray[0]);
	}

	@Test
	public void testCorrectParsingTheResultForFirstMatch() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		String result = source.parseMatchResult(1, content);
		Assert.assertEquals("3-0", result);
	}

	@Test
	public void testCorrectParsingTheResultForLastRound() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		String result = source.parseMatchResult(34, content);
		Assert.assertEquals("3-2", result);
	}

	@Test(expected = MatchResultNotFound.class)
	public void testCorrectParsingTheResultFor35RoundShouldThrowException() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		source.parseMatchResult(35, content);
	}

	@Test(expected = MatchResultNotFound.class)
	public void testCorrectParsingTheResultFor0RoundShouldThrowException() {
		String content = ClasspathUtils.getContentUTF8("crawl-data/resultdb-last-matches-for-team.html");
		source.parseMatchResult(0, content);
	}
}
