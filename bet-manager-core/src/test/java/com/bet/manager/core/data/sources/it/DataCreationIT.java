package com.bet.manager.core.data.sources.it;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.commons.util.DocumentUtils;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ISecondarySource;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.models.dao.MatchMetaData;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataCreationIT {

	private static final String DELIMITER = MatchMetaData.CONSTRUCTOR_PARAMS_SPLITERATOR;

	private ISecondarySource secondarySource = new ResultDB();

	@Test
	public void testGettingDataForMatchFromPreviousSeason() {

		String firstTeam = "FC Bayern München";
		String secondTeam = "VfB Stuttgart";
		int round = 2;

		Document currentRoundDoc = DocumentUtils
				.parse(ClasspathUtils.getContentUTF8("crawl-data/bundesliga_post_standing_2.xml"));

		String prevRoundStats = ClasspathUtils.getContentUTF8("crawl-data/bundesliga_team_stats_round_1.xml");
		Map<String, Integer> rankingTable = Bundesliga.createRankingTable(currentRoundDoc);

		String allMatchesFirstTeamContent =
				ClasspathUtils.getContentUTF8("crawl-data/bayern-munich_2012.html");
		String allMatchesSecondTeamContent =
				ClasspathUtils.getContentUTF8("crawl-data/vfb-stuttgart_2012.html");

		Map<String, Integer> avgStatsPrev = Bundesliga.parseAverageRoundStats(
				ClasspathUtils.getContentUTF8("crawl-data/bundesliga_team_stats_round_1.xml"));

		StringBuilder actual = new StringBuilder();
		actual
				.append(round)
				.append(",")
				.append(rankingTable.get(firstTeam))
				.append(",")
				.append(Bundesliga.parseCurrentRankingStats(firstTeam, currentRoundDoc))
				.append(",")
				.append(secondarySource.parseMatchVenue(allMatchesFirstTeamContent, round))
				.append(",")
				.append(Bundesliga.parseTeamPerformance(prevRoundStats, firstTeam, avgStatsPrev))
				.append(",")
				.append(secondarySource.parseLastFiveGamesForTeam(allMatchesFirstTeamContent, round))
				.append(",")
				.append(rankingTable.get(secondTeam))
				.append(",")
				.append(Bundesliga.parseCurrentRankingStats(secondTeam, currentRoundDoc))
				.append(",")
				.append(secondarySource.parseMatchVenue(allMatchesSecondTeamContent, round))
				.append(",")
				.append(Bundesliga.parseTeamPerformance(prevRoundStats, secondTeam, avgStatsPrev))
				.append(",")
				.append(secondarySource.parseLastFiveGamesForTeam(allMatchesSecondTeamContent, round))
				.append(",")
				.append(secondarySource.parseMatchResult(round, allMatchesFirstTeamContent));

		List<String> performance =
				Arrays.asList("2", "1", "6", "8", "1", "103267", "104", "571", "18", "12", "1", "0", "0", "0", "0", "18", "0", "-6",
						"-1", "120981", "211", "432", "10", "15", "0", "0", "0", "1", "0", "6-1");
		String expected = performance.stream()
				.collect(Collectors.joining(DELIMITER));

		Assert.assertEquals(expected, actual.toString());
	}
}
