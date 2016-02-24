package com.bet.manager.models;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;
import com.bet.manager.models.dao.Match;
import com.bet.manager.models.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.models.exceptions.InvalidMatchDateException;
import com.bet.manager.models.util.FootballMatchBuilder;
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
				.setResult("2-1")
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
				.setResult("2-2")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.TIE_RESULT, match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithUnknownResult() throws ParseException {

		Match match = builder
				.setStartDate(DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse("20160101220000"))
				.setResult("? - ?")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.NO_WINNER, match.getWinner());
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
	public void testMatchWinnerWithNoGivenResult() {
		Assert.assertEquals(ResultMessages.NO_WINNER, builder.setStartDate(new Date()).build().getWinner());
	}
}