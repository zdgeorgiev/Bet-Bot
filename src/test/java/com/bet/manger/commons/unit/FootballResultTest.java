package com.bet.manger.commons.unit;

import com.bet.manager.commons.FootballResult;
import com.bet.manager.commons.ResultMessages;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class FootballResultTest {

	private static FootballResult matchResult;

	@BeforeClass
	public void init() {
		matchResult = new FootballResult("Arsenal", "Liverpool");
	}

	@Test
	public void testGetCorrectWinner() {
		matchResult.setScore("2-1");
		Assert.assertEquals(matchResult.getWinner(), "Arsenal");
	}

	@Test
	public void testCorrectTieResult() {
		matchResult.setScore("2-2");
		Assert.assertEquals(matchResult.getWinner(), ResultMessages.TIE_RESULT);
	}

	@Test
	public void testUnknownResultOutput() {
		matchResult = new FootballResult("Crystal Palace", "Manchester City");
		Assert.assertEquals(matchResult.getScore(), ResultMessages.UNKNOWN_RESULT);
	}

	@Test
	public void testUnknownWinnerOutput() {
		matchResult = new FootballResult("Crystal Palace", "Manchester City");
		Assert.assertEquals(matchResult.getWinner(), ResultMessages.UNKNOWN_WINNER);
	}
}
