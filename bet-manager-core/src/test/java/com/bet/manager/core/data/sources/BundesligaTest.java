package com.bet.manager.core.data.sources;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.commons.util.DocumentUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BundesligaTest {

	private static final String TEAM_ATTR = "team";

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

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectParsingPreviousRoundStatsForTeams() {

		Map<String, Integer> actual = Bundesliga.parseAverageRoundStats(
				ClasspathUtils.getContentUTF8("crawl-data/bundesliga-round-stats.xml"));

		Map<String, Integer> expected = new HashMap<>();

		expected.put("imp:tracking-distance", 111923);
		expected.put("imp:tracking-sprints", 191);
		expected.put("imp:passes-total", 393);
		expected.put("shots-total", 12);
		expected.put("fouls-committed", 14);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCorrectStatsForMatch() {

		String prevRoundTeamStatsXML =
				ClasspathUtils.getContentUTF8("crawl-data/bundesliga-stats-for-matches.xml");

		Map<String, Integer> prevRoundStats = new HashMap<>();
		prevRoundStats.put("imp:tracking-distance", 111923);
		prevRoundStats.put("imp:tracking-sprints", 191);
		prevRoundStats.put("imp:passes-total", 393);
		prevRoundStats.put("shots-total", 12);
		prevRoundStats.put("fouls-committed", 14);

		Map<String, Integer> actual = Bundesliga.parseTeamPerformance(prevRoundTeamStatsXML, "1.FSV Mainz 05", prevRoundStats);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put("distance", 125042);
		expected.put("sprints", 184);
		expected.put("passes", 320);
		expected.put("shots", 13);
		expected.put("fouls", 11);

		Assert.assertEquals(expected, actual);
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

		Map<String, Integer> actual = Bundesliga.parseTeamPerformance(prevRoundTeamStatsXML, "FC Bayern München", prevRoundStats);

		Map<String, Integer> expected = new LinkedHashMap<>();
		expected.put("distance", 111923);
		expected.put("sprints", 189);
		expected.put("passes", 579);
		expected.put("shots", 21);
		expected.put("fouls", 15);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGoalDifferenceForRound1() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-bayern-round-1.xml");
		Document doc = DocumentUtils.parse(content);

		int actual = Bundesliga.parseGoalDifference(doc.getElementsByTagName(TEAM_ATTR).item(0));
		Assert.assertEquals(-1, actual);
	}

	@Test
	public void testGoalDifferenceForRound15() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-borussia-round-15.xml");
		Document doc = DocumentUtils.parse(content);

		int actual = Bundesliga.parseGoalDifference(doc.getElementsByTagName(TEAM_ATTR).item(0));
		Assert.assertEquals(14, actual);
	}

	@Test
	public void testRankingStatsForTeamForRound1() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-bayern-round-1.xml");
		Document doc = DocumentUtils.parse(content);

		int actual = Bundesliga.parsePoints(doc.getElementsByTagName(TEAM_ATTR).item(0));
		Assert.assertEquals(0, actual);
	}

	@Test
	public void testRankingStatsForTeamForRound15() {

		String content = ClasspathUtils.getContentUTF8("crawl-data/bundesliga-borussia-round-15.xml");
		Document doc = DocumentUtils.parse(content);

		int actual = Bundesliga.parsePoints(doc.getElementsByTagName(TEAM_ATTR).item(0));
		Assert.assertEquals(30, actual);
	}
}
