package com.bet.manager.core.unit;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.util.ResourceUtils;
import com.bet.manager.core.util.LiveScoreMatchUtils;
import com.bet.manager.model.dao.Match;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;

public class LiveScoreMatchUtilsTest {

	@Test
	public void testValidMatchParsingFromHtml() throws ParseException {
		String content = ResourceUtils.getContent("live-score-match.txt", LiveScoreMatchUtils.class);
		Element matchDiv = Jsoup.parse(content);

		Match expected = new FootballMatchBuilder()
				.setHomeTeamName("Tottenham Hotspur")
				.setAwayTeamName("Sunderland")
				.setStartDate(
						DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 1 2016 14:45"))
				.build();

		Match actual = LiveScoreMatchUtils.parse(matchDiv, "January 1");

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testInvalidMatchContentParsing() throws ParseException {
		String content = ResourceUtils.getContent("live-score-same-teams.txt", LiveScoreMatchUtils.class);
		Element matchDiv = Jsoup.parse(content);

		Assert.assertNull(LiveScoreMatchUtils.parse(matchDiv, "January 1"));
	}

	@Test
	public void testInvalidDate() throws ParseException {
		String content = ResourceUtils.getContent("live-score-match.txt", LiveScoreMatchUtils.class);
		Element matchDiv = Jsoup.parse(content);

		Assert.assertNull(LiveScoreMatchUtils.parse(matchDiv, "6 January"));
	}
}
