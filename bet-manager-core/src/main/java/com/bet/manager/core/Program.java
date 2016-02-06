package com.bet.manager.core;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.model.FootballMatch;
import com.bet.manager.model.dao.Match;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Program {

	public static void main(String[] args) throws IOException, ParseException {

		//		BasicConfigurator.configure();
		//
		//		IWebCrawler crawler = new LiveScoreCrawler();
		//		IMatchParser parser = new LiveScoreMatchParser();
		//
		//		List<Match> matches =
		//				parser.parse(
		//						crawler.crawl(
		//								new URL("http://www.livescore.com/soccer/germany/2-bundesliga/results/30-days/")));
		//
		//		matches.stream().forEach((x) -> System.out.println(x));

		File file = new File("bet-manager-core/src/test/resources/live-score-full-content-with-no-results.txt");
		System.out.println(file.exists());

		List<Match> matches = new LiveScoreMatchParser()
				.parse(IOUtils.toString(new BufferedInputStream(new FileInputStream(file)), "UTF-8"));

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

		Assert.assertEquals(matches, expected);
	}

	private static FootballMatch createMatch(String homeTeam, String awayTeam, String date) {

		Date startDate;

		try {
			startDate = DateFormats.LIVE_SCORE_MATCH_DATE_FORMATTED.parse(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Start date cannot be parsed.");
		}

		return new FootballMatchBuilder()
				.setHomeTeamName(homeTeam)
				.setAwayTeamName(awayTeam)
				.setStartDate(startDate)
				.build();
	}
}
