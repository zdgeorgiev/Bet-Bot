package com.bet.manager.model;

import com.bet.manager.model.util.FootballResultBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FootballResultBuilderTest {

	private static FootballResultBuilder builder;

	@BeforeClass
	public void init() {
		builder = new FootballResultBuilder();
	}

	@Test
	public void testGetCorrectWinnerCodeForHomeWin() {
		FootballResult m = builder.setScore("2-1").build();
		Assert.assertEquals(m.getWinnerCode(), 1);
	}

	@Test
	public void testGetCorrectWinnerCodeForAwayWin() {
		FootballResult m = builder.setScore("2-3").build();
		Assert.assertEquals(m.getWinnerCode(), 2);
	}

	@Test
	public void testCorrectWinnerCodeFromTieScore() {
		FootballResult m = builder.setScore("2-2").build();
		Assert.assertEquals(m.getWinnerCode(), 0);
	}

	@Test
	public void testWithUnknownScore() {
		FootballResult m = builder.setScore("").build();
		Assert.assertEquals(m.getWinnerCode(), -1);
	}

	@Test
	public void testWithUnknownScore2() {
		FootballResult m = builder.setScore("? - ?").build();
		Assert.assertEquals(m.getWinnerCode(), -1);
	}
}
