import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.core.DataCrawler;
import com.bet.manager.core.DocumentUtils;
import org.junit.Test;
import org.testng.Assert;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class DataCrawlerTest {

	@Test
	public void testCorrectRankingParsing() {

		Document doc = DocumentUtils.parse(ClasspathUtils.getContentUTF8("crawl-data/bundesliga-round.xml"));
		Map<String, Integer> actual = DataCrawler.parseRoundRanking(doc);

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

		Map<String, Double> actual =
				DataCrawler
						.getAverageRoundStats(ClasspathUtils.getContentISO("crawl-data/bundesliga-round-stats.xml"));

		Map<String, Double> expected = new HashMap<>();

		expected.put("imp:tracking-distance", 111923.72);
		expected.put("imp:tracking-sprints", 191.69);
		expected.put("imp:passes-total", 393.78);
		expected.put("shots-total", 12.54);
		expected.put("fouls-committed", 14.94);

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testCorrectGettingTheFirstRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String[] actual = DataCrawler.getCurrentTeamOpponentAndVenue(content, 2);
		String[] expected = new String[] { "Mönchengladbach", "Away" };
		Assert.assertEquals(actual, expected);
	}

	@Test
	public void testCorrectGettingThirdRoundOpponent() {

		String content = ClasspathUtils.getContentISO("crawl-data/resultdb-last-matches-for-team.html");
		String[] actual = DataCrawler.getCurrentTeamOpponentAndVenue(content, 3);
		String[] expected = new String[] { "Bayer Leverkusen", "Home" };
		Assert.assertEquals(actual, expected);
	}
}
