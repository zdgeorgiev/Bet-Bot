package com.bet.manager.core.it;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.LiveScoreMatchParser;
import com.bet.manager.models.FootballMatch;
import com.bet.manager.models.dao.Match;
import com.bet.manager.models.util.FootballMatchBuilder;
import com.bet.manager.models.util.FootballMatchUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveScoreMatchParserIT {

	private static LiveScoreMatchParser parser;

	@BeforeClass
	public static void init() {
		parser = new LiveScoreMatchParser();
	}

	@Test
	public void testWithFullContentWithMatchesWithoutResults() {
		String content =
				ClasspathUtils.getContentUTF8("live-score-full-content-with-no-results.txt");

		List<Match> actual = parser.parse(content);

		List<Match> expected = new ArrayList<Match>() {
			{
				//12 January
				add(createMatch("AFC Bournemouth", "West Ham United", "January 12 2016 21:45"));
				add(createMatch("Aston Villa", "Crystal Palace", "January 12 2016 21:45"));
				add(createMatch("Newcastle United", "Manchester United", "January 12 2016 21:45"));

				//13 January
				add(createMatch("Chelsea", "West Bromwich Albion", "January 13 2016 21:45"));
				add(createMatch("Manchester City", "Everton", "January 13 2016 21:45"));
				add(createMatch("Southampton", "Watford", "January 13 2016 21:45"));
				add(createMatch("Stoke City", "Norwich City", "January 13 2016 21:45"));
				add(createMatch("Swansea City", "Sunderland", "January 13 2016 21:45"));
				add(createMatch("Liverpool", "Arsenal", "January 13 2016 22:00"));
				add(createMatch("Tottenham Hotspur", "Leicester City", "January 13 2016 22:00"));
			}
		};

		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertTrue(FootballMatchUtils.equals(
					(FootballMatch) actual.get(i),
					(FootballMatch) expected.get(i)));
		}
	}

	@Test
	public void testWithFullContentWithMatchesWithResults() {
		String content =
				ClasspathUtils.getContentUTF8("live-score-matches-with-results.txt");

		List<Match> actual = parser.parse(content);

		List<Match> expected = new ArrayList<Match>() {
			{
				Match m = createMatch("AFC Bournemouth", "West Ham United", "January 12 2016 21:45");
				FootballMatchUtils.setResultAndWinner(m, "3-3");
				add(m);

				Match m2 = createMatch("Aston Villa", "Crystal Palace", "January 12 2016 21:45");
				FootballMatchUtils.setResultAndWinner(m2, "2-1");
				add(m2);
			}
		};

		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertTrue(FootballMatchUtils.equals(
					(FootballMatch) actual.get(i),
					(FootballMatch) expected.get(i)));
		}
	}

	@Test
	public void testWithFullContentWithMatchesWithResultsAndSomeWithout() {
		String content =
				ClasspathUtils.getContentUTF8("live-score-matches-with-results-and-without.txt");

		List<Match> actual = parser.parse(content);

		List<Match> expected = new ArrayList<Match>() {
			{
				Match m = createMatch("AFC Bournemouth", "West Ham United", "January 12 2016 21:45");
				FootballMatchUtils.setResultAndWinner(m, "3-3");
				add(m);

				add(createMatch("Aston Villa", "Crystal Palace", "January 12 2016 21:45"));

				Match m2 = createMatch("Levski", "CSKA", "January 12 2016 21:45");
				FootballMatchUtils.setResultAndWinner(m2, "2-3");
				add(m2);
			}
		};

		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertTrue(FootballMatchUtils.equals(
					(FootballMatch) actual.get(i),
					(FootballMatch) expected.get(i)));
		}
	}

	@Test
	public void testWithFullContentButOnlyOneValidMatchOutOf5() {
		String content = ClasspathUtils
				.getContentUTF8("live-score-full-content-with-only-one-valid-match.txt");

		List<Match> actual = parser.parse(content);

		Match expected = createMatch("Manchester City", "Everton", "January 13 2016 21:45");

		Assert.assertEquals(actual.size(), 1);
		Assert.assertTrue(FootballMatchUtils.equals(
				(FootballMatch) actual.get(0),
				(FootballMatch) expected));
	}

	private FootballMatch createMatch(String homeTeam, String awayTeam, String date) {

		Date startDate;

		try {
			startDate = DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Start date cannot be parsed.");
		}

		return new FootballMatchBuilder()
				.setHomeTeamName(homeTeam)
				.setAwayTeamName(awayTeam)
				.setStartDate(startDate)
				.build();
	}
}