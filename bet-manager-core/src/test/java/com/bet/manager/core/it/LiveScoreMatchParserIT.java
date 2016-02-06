package com.bet.manager.core.it;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.util.ResourceUtils;
import com.bet.manager.core.LiveScoreMatchParser;
import com.bet.manager.model.FootballMatch;
import com.bet.manager.model.dao.Match;
import com.bet.manager.model.util.FootballMatchBuilder;
import com.bet.manager.model.util.FootballResultBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
				ResourceUtils.getContent("live-score-full-content-with-no-results.txt", LiveScoreMatchParser.class);

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

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testWithFullContentWithMatchesWithResults() {
		String content =
				ResourceUtils.getContent("live-score-full-content-with-results.txt", LiveScoreMatchParser.class);

		List<Match> actual = parser.parse(content);

		List<Match> expected = new ArrayList<Match>() {
			{
				Match m = createMatch("AFC Bournemouth", "West Ham United", "January 12 2016 21:45");
				m.setResult(new FootballResultBuilder().setScore("3-3").build());
				add(m);

				Match m2 = createMatch("Aston Villa", "Crystal Palace", "January 12 2016 21:45");
				m2.setResult(new FootballResultBuilder().setScore("2-1").build());
				add(m2);
			}
		};

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testWithFullContentButOnlyOneValidMatchOutOf5() {
		String content = ResourceUtils
				.getContent("live-score-full-content-with-only-one-valid-match.txt", LiveScoreMatchParser.class);

		List<Match> actual = parser.parse(content);

		Match expected = createMatch("Manchester City", "Everton", "January 13 2016 21:45");

		Assert.assertEquals(actual.size(), 1);
		Assert.assertEquals(actual.get(0), expected);
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