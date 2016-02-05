package com.bet.manager.model;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.util.FootballResultBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FootballResultBuilderTest {

	private static FootballResultBuilder matchResult;

	@BeforeClass
	public void init() {
		matchResult = new FootballResultBuilder().setHomeTeam("Arsenal").setAwayTeam("Liverpool");
	}

	@Test
	public void testGetCorrectWinner() {
		FootballResult m = matchResult.setScore("2-1").build();
		Assert.assertEquals(m.getWinner(), "Arsenal");
	}

	@Test
	public void testCorrectTieScore() {
		FootballResult m = matchResult.setScore("2-2").build();
		Assert.assertEquals(m.getScore(), "2-2");
	}

	@Test
	public void testUnknownScore() {
		FootballResult m =
				new FootballResultBuilder().setHomeTeam("Arsenal").setAwayTeam("Liverpool").build();
		Assert.assertEquals(m.getScore(), ResultMessages.UNKNOWN_SCORE);
	}

	@Test
	public void testUnknownWinnerOutput() {
		FootballResult m =
				new FootballResultBuilder().setHomeTeam("Arsenal").setAwayTeam("Liverpool").build();
		Assert.assertEquals(m.getWinner(), ResultMessages.UNKNOWN_WINNER);
	}
}
