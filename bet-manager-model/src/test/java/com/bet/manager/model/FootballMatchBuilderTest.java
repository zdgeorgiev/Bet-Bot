package com.bet.manager.model;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.model.util.FootballMatchBuilder;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

public class FootballMatchBuilderTest {

	private static FootballMatchBuilder builder;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");

	@Before
	public void init() {
		builder = new FootballMatchBuilder()
				.setHomeTeamName("Levski")
				.setAwayTeamName("CSKA");
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithHomeTeamWinner() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateTime.parse("2016-01-01T22:00"))
				.setResult("2-1")
				.setStatus(MatchStatus.FINISHED)
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("2016-01-01T22:00", DATE_FORMATTER.print(match.getStartDate()));
		Assert.assertEquals(match.getHomeTeam(), match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithTieResult() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateTime.parse("2016-01-01T22:00"))
				.setResult("2-2")
				.setStatus(MatchStatus.FINISHED)
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("2016-01-01T22:00", DATE_FORMATTER.print(match.getStartDate()));
		Assert.assertEquals(ResultMessages.TIE_RESULT, match.getWinner());
	}

	@Test
	public void testMatchBuilderWithValidHomeAwayTeamsAndDateWithUnknownResult() throws ParseException {

		FootballMatch match = builder
				.setStartDate(DateTime.parse("2016-01-01T22:00"))
				.setResult("? - ?")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("2016-01-01T22:00", DATE_FORMATTER.print(match.getStartDate()));
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, match.getWinner());
	}

	@Test(expected = EqualHomeAndAwayTeamException.class)
	public void testMatchWithSameHomeAndAwayTeam() throws ParseException {
		builder.setAwayTeamName("Levski").build();
	}

	@Test(expected = IllegalFieldValueException.class)
	public void testMatchWithInvalidStartDate() throws ParseException {
		builder.setStartDate(DateTime.parse("2016-13-10T12:20"));
	}

	@Test
	public void testMatchWinnerWithNoGivenResult() {
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, builder.setStartDate(new DateTime()).build().getWinner());
	}
}
