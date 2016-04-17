package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.util.DocumentUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class BundesligaTest {

	@Test
	public void testCorrectRankingParsing() {

		Document doc = DocumentUtils.parse(ClasspathUtils.getContentUTF8("crawl-data/bundesliga-round.xml"));
		Map<String, Integer> actual = Bundesliga.createRankingTable(doc);

		Map<String, Integer> expected = new HashMap<>();
		expected.put("VfB Stuttgart", 1);
		expected.put("VfL Wolfsburg", 2);
		expected.put("Borussia Dortmund", 3);
		expected.put("1.FSV Mainz 05", 4);
		expected.put("SV Werder Bremen", 5);
		expected.put("Hannover 96", 6);
		expected.put("1.FC Nürnberg", 7);
		expected.put("Borussia M'gladbach", 8);
		expected.put("FC Augsburg", 9);
		expected.put("SC Freiburg", 10);
		expected.put("1899 Hoffenheim", 11);
		expected.put("FC Bayern München", 12);
		expected.put("Hertha BSC", 13);
		expected.put("Hamburger SV", 14);
		expected.put("1.FC Kaiserslautern", 15);
		expected.put("Bayer 04 Leverkusen", 16);
		expected.put("1.FC Köln", 17);
		expected.put("FC Schalke 04", 18);

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testCorrectParsingPreviousRoundStatsForTeams() {

		Map<String, Integer> actual =
				Bundesliga
						.parseAverageRoundStats(ClasspathUtils.getContentUTF8("crawl-data/bundesliga-round-stats.xml"));

		Map<String, Integer> expected = new HashMap<>();

		expected.put("imp:tracking-distance", 111923);
		expected.put("imp:tracking-sprints", 191);
		expected.put("imp:passes-total", 393);
		expected.put("shots-total", 12);
		expected.put("fouls-committed", 14);

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testCorrectStatsForMatch() {

		String prevRoundTeamStatsXML = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-stats-for-matches.xml");

		Map<String, Integer> prevRoundStats = new HashMap<>();
		prevRoundStats.put("imp:tracking-distance", 111923);
		prevRoundStats.put("imp:tracking-sprints", 191);
		prevRoundStats.put("imp:passes-total", 393);
		prevRoundStats.put("shots-total", 12);
		prevRoundStats.put("fouls-committed", 14);

		String actual =
				Bundesliga.parsePrevRoundTeamPerformance(prevRoundTeamStatsXML, "1.FSV Mainz 05", prevRoundStats);
		String expected = "125042 184 320 13 11";

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testCorrectStatsForMatchWhichHaveEmptyDistanceAttributeAndShouldGetFromThePrevRound() {

		String prevRoundTeamStatsXML = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-stats-for-matches.xml");

		Map<String, Integer> prevRoundStats = new HashMap<>();
		prevRoundStats.put("imp:tracking-distance", 111923);
		prevRoundStats.put("imp:tracking-sprints", 191);
		prevRoundStats.put("imp:passes-total", 393);
		prevRoundStats.put("shots-total", 12);
		prevRoundStats.put("fouls-committed", 14);

		String actual =
				Bundesliga
						.parsePrevRoundTeamPerformance(prevRoundTeamStatsXML, "FC Bayern München", prevRoundStats);
		String expected = "111923 189 579 21 15";

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testRankingStatsForTeamForRound1() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-round.xml");
		Document doc = DocumentUtils.parse(content);

		String actual = Bundesliga.parseCurrentRankingStats("FC Bayern München", doc);
		String expected = "0 -1";

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testRankingStatsForTeamForRound15() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-stats-for-matches-round-15.xml");
		Document doc = DocumentUtils.parse(content);

		String actual = Bundesliga.parseCurrentRankingStats("Borussia M'gladbach", doc);
		String expected = "30 14";

		Assert.assertEquals(actual, expected);
	}
}
