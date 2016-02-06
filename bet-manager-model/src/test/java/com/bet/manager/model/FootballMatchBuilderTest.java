package com.bet.manager.model;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.dao.Match;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.model.exceptions.InvalidMatchDateException;
import com.bet.manager.model.util.FootballMatchBuilder;
import com.bet.manager.model.util.FootballResultBuilder;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.Date;

public class FootballMatchBuilderTest {

	private FootballMatchBuilder builder;

	@BeforeMethod
	public void init() {
		builder = new FootballMatchBuilder()
				.setHomeTeamName("Levski")
				.setAwayTeamName("CSKA");
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithHomeTeamWinner() throws ParseException {

		Match match = builder
				.setStartDate(DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse("20160101220000"))
				.setResult(new FootballResultBuilder().setScore("2-1").build())
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(match.getHomeTeam(), match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithTieResult() throws ParseException {

		Match match = builder
				.setStartDate(DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse("20160101220000"))
				.setResult(new FootballResultBuilder().setScore("2-2").build())
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.TIE, match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithUnknownResult() throws ParseException {

		Match match = builder
				.setStartDate(DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse("20160101220000"))
				.setResult(new FootballResultBuilder().setScore("? - ?").build())
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, match.getWinner());
	}

	@Test(expectedExceptions = EqualHomeAndAwayTeamException.class)
	public void testMatchWithSameHomeAndAwayTeam() throws ParseException {
		builder.setAwayTeamName("Levski").build();
	}

	@Test(expectedExceptions = ParseException.class)
	public void testMatchWithInvalidStartDate() throws ParseException {
		builder.setStartDate(DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse("20165101220000"));
	}

	@Test(expectedExceptions = InvalidMatchDateException.class)
	public void testMatchWithNotGivenStartDate() {
		builder.build();
	}

	@Test
	public void testMatchNoGivenResult() {
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, builder.setStartDate(new Date()).build().getWinner());
	}
}
