package com.bet.manager.model;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import com.bet.manager.model.exceptions.EqualHomeAndAwayTeamException;
import com.bet.manager.model.exceptions.MissingMandatoryPropertyException;
import com.bet.manager.model.util.FootballMatchBuilder;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FootballMatchBuilderTest {

	private static FootballMatchBuilder builder;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Before
	public void init() {
		builder = new FootballMatchBuilder()
				.setHomeTeamName("Levski")
				.setAwayTeamName("CSKA")
				.setYear(2017)
				.setRound(4);
	}

	@Test
	public void testCreateCorrectMatch() {

		FootballMatch match = builder
				.setStartDate(LocalDateTime.parse("2016-01-01T22:00:00", DATE_FORMATTER))
				.setResult("2-1")
				.setStatus(MatchStatus.FINISHED)
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals("2016-01-01T22:00:00", DATE_FORMATTER.format(match.getStartDate()));
		Assert.assertEquals("2-1", match.getResult());
		Assert.assertEquals(match.getHomeTeam(), match.getWinner());
	}

	@Test
	public void testCreateMatchWithTieResult() {

		FootballMatch match = builder
				.setResult("2-2")
				.setStatus(MatchStatus.FINISHED)
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals(ResultMessages.TIE_RESULT, match.getWinner());
	}

	@Test
	public void notFinishedMatchMustNotHaveWinner() {

		FootballMatch match = builder
				.setResult("2-2")
				.build();

		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, match.getWinner());
	}

	@Test
	public void matchWithNoGivenResultMustHaveUnknownWinner() {
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, builder.build().getWinner());
	}

	@Test
	public void testMatchWithUnknownResultMustHaveUnknownWinner() {

		FootballMatch match = builder
				.setResult("? - ?")
				.build();

		Assert.assertEquals("Levski", match.getHomeTeam());
		Assert.assertEquals("CSKA", match.getAwayTeam());
		Assert.assertEquals(ResultMessages.UNKNOWN_WINNER, match.getWinner());
	}

	@Test(expected = EqualHomeAndAwayTeamException.class)
	public void testMatchWithSameHomeAndAwayTeam() {
		builder.setAwayTeamName("Levski").build();
	}

	@Test(expected = DateTimeException.class)
	public void matchWithInvalidStartDateShouldThrowException() {
		builder.setStartDate(LocalDateTime.parse("2016-13-10T12:20:00", DATE_FORMATTER)).build();
	}

	@Test(expected = MissingMandatoryPropertyException.class)
	public void matchWithoutHomeTeamCannotBeCreated() {
		new FootballMatchBuilder().setAwayTeamName(".").setYear(2017).setRound(3).build();
	}

	@Test(expected = MissingMandatoryPropertyException.class)
	public void matchWithoutAwayTeamCannotBeCreated() {
		new FootballMatchBuilder().setHomeTeamName(".").setYear(2017).setRound(3).build();
	}

	@Test(expected = MissingMandatoryPropertyException.class)
	public void matchWithoutRoundCannotBeCreated() {
		new FootballMatchBuilder().setAwayTeamName(".").setYear(2017).build();
	}

	@Test(expected = MissingMandatoryPropertyException.class)
	public void matchWithoutYearCannotBeCreated() {
		new FootballMatchBuilder().setAwayTeamName(".").setRound(3).build();
	}

	@Test
	public void correctUpdateOfAField() {
		FootballMatch updated = builder.setResult("2-1").updatedStatus(MatchStatus.FINISHED).build();
		Assert.assertEquals(updated.getHomeTeam(), updated.getWinner());
	}

	@Test
	public void testCorrectPredictionIsSet() {
		FootballMatch updated = builder.setResult("2-1")
				.updatedStatus(MatchStatus.FINISHED)
				.setPrediction("Levski")
				.build();

		Assert.assertEquals(PredictionType.CORRECT, updated.getPredictionType());
	}

	@Test
	public void matchWithoutResultMustHaveUnknownResult() {
		Assert.assertEquals(ResultMessages.UNKNOWN_RESULT, builder.build().getResult());
	}

	@Test
	public void matchWithoutPredictionMustHaveNotPredicted() {
		Assert.assertEquals(PredictionType.NOT_PREDICTED, builder.build().getPredictionType());
	}

	@Test
	public void testCorrectUpdate() {
		FootballMatch initialMatch = new FootballMatchBuilder()
				.setHomeTeamName("Levski")
				.setAwayTeamName("CSKA")
				.setYear(2017)
				.setRound(4)
				.build();

		new FootballMatchBuilder(initialMatch).updatedStatus(MatchStatus.STARTED).build();

		Assert.assertEquals(MatchStatus.STARTED, initialMatch.getMatchStatus());
	}
}
