package com.bet.manger.commons.unit;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.FootballMatch;
import com.bet.manager.commons.Match;
import com.bet.manager.utils.LiveScoreMatchUtils;
import com.bet.manager.utils.ResourceUtils;
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

		Match expected = new FootballMatch("Tottenham Hotspur", "Sunderland",
				DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse("January 1 2016 14:45"));
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
