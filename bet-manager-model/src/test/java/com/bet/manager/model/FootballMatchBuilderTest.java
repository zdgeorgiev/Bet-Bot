package com.bet.manager.model;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.model.util.FootballMatchBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

public class FootballMatchBuilderTest {

	private static FootballMatchBuilder builder;

	@Before
	public void init() {
		builder = new FootballMatchBuilder()
				.setHomeTeamName("Levski")
				.setAwayTeamName("CSKA");
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithHomeTeamWinner() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateFormats.CONCATED_DATE_AND_TIME.parse("20160101220000"))
				.setResult("2-1")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.MATCH_DATE_AND_TIME_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(match.getHomeTeam(), match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithTieResult() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateFormats.CONCATED_DATE_AND_TIME.parse("20160101220000"))
				.setResult("2-2")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.MATCH_DATE_AND_TIME_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.TIE_RESULT, match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithUnknownResult() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateFormats.CONCATED_DATE_AND_TIME.parse("20160101220000"))
				.setResult("? - ?")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("January 1 2016 22:00",
				DateFormats.MATCH_DATE_AND_TIME_FORMATTED.format(match.getStartDate()));
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, match.getWinner());
	}

	@Test(expected = EqualHomeAndAwayTeamException.class)
	public void testMatchWithSameHomeAndAwayTeam() throws ParseException {
		builder.setAwayTeamName("Levski").build();
	}

	@Test(expected = ParseException.class)
	public void testMatchWithInvalidStartDate() throws ParseException {
		builder.setStartDate(DateFormats.CONCATED_DATE_AND_TIME.parse("20165101220000"));
	}

	@Test
	public void testMatchWinnerWithNoGivenResult() {
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, builder.setStartDate(new Date()).build().getWinner());
	}
}
