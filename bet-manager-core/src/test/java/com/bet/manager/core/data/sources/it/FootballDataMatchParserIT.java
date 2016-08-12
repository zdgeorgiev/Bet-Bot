package com.bet.manager.core.data.sources.it;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.FootballDataMatchParser;
import com.bet.manager.core.IMatchParser;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FootballDataMatchParserIT {

	private static IMatchParser parser;

	@BeforeClass
	public static void init() {
		parser = new FootballDataMatchParser();
	}

	@Test
	public void skipAllMatchesForMatchDayOne() {

		// 18 matches with 9 for round 1
		String content = ClasspathUtils.getContentUTF8("footballData-matches-for-different-matchday.txt");

		Map<MatchStatus, List<FootballMatch>> actual = parser.parse(content);

		Assert.assertNotNull(actual);
		Assert.assertEquals(9, actual.get(MatchStatus.NOT_STARTED).size());
	}

	@Test
	public void testWithFullContentWithMatchesWithoutResults() {

		String content = ClasspathUtils.getContentUTF8("footballData-matches-samples.txt");

		Map<MatchStatus, List<FootballMatch>> actual = parser.parse(content);

		List<FootballMatch> expected = new ArrayList<FootballMatch>() {
			{
				add(createMatch(2016, 2, "Bayer 04 Leverkusen", "Hamburger SV", "2016-09-10T13:30:00Z"));
				add(createMatch(2016, 2, "SV Darmstadt 98", "Eintracht Frankfurt", "2016-09-10T13:30:00Z"));
				add(createMatch(2016, 2, "SC Freiburg", "Borussia M'gladbach", "2016-09-10T13:30:00Z"));
				add(createMatch(2016, 2, "1.FSV Mainz 05", "1899 Hoffenheim", "2016-09-10T13:30:00Z"));
			}
		};

		assertEqualNotFinishedMatches(actual, expected);
	}

	@Test
	public void testWithFullContentWithMatchesWithResults() {

		String content = ClasspathUtils.getContentUTF8("footballData-matches-with-results.txt");

		Map<MatchStatus, List<FootballMatch>> actual = parser.parse(content);

		List<FootballMatch> expected = new ArrayList<FootballMatch>() {
			{
				add(new FootballMatchBuilder(createMatch(2016, 2, "Bayer 04 Leverkusen", "Hamburger SV", "2016-09-10T13:30:00Z"))
						.setResult("2-2").build());
				add(new FootballMatchBuilder(createMatch(2016, 2, "SV Darmstadt 98", "Eintracht Frankfurt", "2016-09-10T13:30:00Z"))
						.setResult("3-5").build());
				add(new FootballMatchBuilder(createMatch(2016, 2, "SC Freiburg", "Borussia M'gladbach", "2016-09-10T13:30:00Z"))
						.setResult("1-1").build());
				add(new FootballMatchBuilder(createMatch(2016, 2, "1.FSV Mainz 05", "1899 Hoffenheim", "2016-09-10T13:30:00Z"))
						.setResult("2-1").build());
			}
		};

		assertEqualFinishedMatches(actual, expected);
	}

	@Test
	public void testCorrectDeterminateMatchesByTheirMatchStatus() {

		String content = ClasspathUtils.getContentUTF8("footballData-matches-with-different-statuses.txt");

		Map<MatchStatus, List<FootballMatch>> actual = parser.parse(content);

		List<FootballMatch> finished = new ArrayList<FootballMatch>() {
			{
				add(new FootballMatchBuilder(createMatch(2016, 2, "Bayer 04 Leverkusen", "Hamburger SV", "2016-09-10T13:30:00Z"))
						.setResult("2-2").build());
				add(new FootballMatchBuilder(createMatch(2016, 2, "SV Darmstadt 98", "Eintracht Frankfurt", "2016-09-10T13:30:00Z"))
						.setResult("3-5").build());
			}
		};

		List<FootballMatch> notFinished = new ArrayList<FootballMatch>() {
			{
				add(createMatch(2016, 2, "SC Freiburg", "Borussia M'gladbach", "2016-09-10T13:30:00Z"));
			}
		};

		List<FootballMatch> started = new ArrayList<FootballMatch>() {
			{

				add(createMatch(2016, 2, "1.FSV Mainz 05", "1899 Hoffenheim", "2016-09-10T13:30:00Z"));
			}
		};

		assertEqualFinishedMatches(actual, finished);
		assertEqualNotFinishedMatches(actual, notFinished);
		assertEqualStartedMatches(actual, started);
	}

	private void assertEqualStartedMatches(Map<MatchStatus, List<FootballMatch>> actual, List<FootballMatch> expected) {
		Assert.assertEquals(actual.get(MatchStatus.STARTED).size(), expected.size());
		for (int i = 0; i < actual.get(MatchStatus.STARTED).size(); i++) {
			Assert.assertEquals(actual.get(MatchStatus.STARTED).get(i), expected.get(i));
		}
	}

	private void assertEqualNotFinishedMatches(Map<MatchStatus, List<FootballMatch>> actual, List<FootballMatch> expected) {
		Assert.assertEquals(actual.get(MatchStatus.NOT_STARTED).size(), expected.size());
		for (int i = 0; i < actual.get(MatchStatus.NOT_STARTED).size(); i++) {
			Assert.assertEquals(actual.get(MatchStatus.NOT_STARTED).get(i), expected.get(i));
		}
	}

	private void assertEqualFinishedMatches(Map<MatchStatus, List<FootballMatch>> actual, List<FootballMatch> expected) {
		Assert.assertEquals(actual.get(MatchStatus.FINISHED).size(), expected.size());
		for (int i = 0; i < actual.get(MatchStatus.FINISHED).size(); i++) {
			Assert.assertEquals(actual.get(MatchStatus.FINISHED).get(i), expected.get(i));
		}
	}

	private FootballMatch createMatch(Integer year, Integer round, String homeTeam, String awayTeam, String date) {

		Date startDate;

		try {
			startDate = DateFormats.FOOTBALL_DATA_DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Start date cannot be parsed.");
		}

		return new FootballMatchBuilder()
				.setHomeTeamName(homeTeam)
				.setAwayTeamName(awayTeam)
				.setStartDate(startDate)
				.setRound(round)
				.setYear(year)
				.build();
	}
}