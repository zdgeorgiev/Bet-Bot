package com.bet.manager.core.it;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.util.ResourceUtils;
import com.bet.manager.core.IMatchParser;
import com.bet.manager.core.LiveScoreMatchParser;
import com.bet.manager.model.FootballMatch;
import com.bet.manager.model.dao.Match;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LiveScoreMatchParserIT {

	private static IMatchParser parser;

	@BeforeClass
	public static void init() {
		parser = new LiveScoreMatchParser();
	}

	@Test
	public void testWithFullContent() throws ParseException {
		String content = ResourceUtils.getContent("live-score-full-content.txt", LiveScoreMatchParser.class);

		List<Match> actual = parser.parse(content);

		List<Match> expected = new ArrayList<Match>() {
			{
				//12 January
				add(createMatch("AFC Bournemouth", "West Ham United", "January 12 2016 21:45"));
				add(createMatch("Aston Villa", "Crystal Palace", "January 12 2016 21:45"));
				add(createMatch("Newcastle United", "Manchester United", "January 12 2016 21:45"));

				//13 January
				add(createMatch("Chelsea", "West Bromwich Albion", "January 13 2016 21:45"));
				add(createMatch("Manchester City", "Everton", "January 13 2016 21:45"));
				add(createMatch("Southampton", "Watford", "January 13 2016 21:45"));
				add(createMatch("Stoke City", "Norwich City", "January 13 2016 21:45"));
				add(createMatch("Swansea City", "Sunderland", "January 13 2016 21:45"));
				add(createMatch("Liverpool", "Arsenal", "January 13 2016 22:00"));
				add(createMatch("Tottenham Hotspur", "Leicester City", "January 13 2016 22:00"));
			}
		};

		Assert.assertTrue(actual.equals(expected));
	}

	private FootballMatch createMatch(String homeTeam, String awayTeam, String date) throws ParseException {
		return new FootballMatchBuilder()
				.setHomeTeamName(homeTeam)
				.setAwayTeamName(awayTeam)
				.setStartDate(
						DateFormats.LiVE_SCORE_MATCH_DATE_FORMAT.parse(date))
				.build();
	}
}