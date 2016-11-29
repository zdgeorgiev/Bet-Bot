package com.bet.manager.core.data.sources.it;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.commons.util.DocumentUtils;
import com.bet.manager.core.data.sources.Bundesliga;
import com.bet.manager.core.data.sources.ResultDB;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataCreationIT {

	@Test
	public void testCreatingCorrectMetaDataForMatch() {

		String firstTeam = "FC Bayern München";
		String secondTeam = "VfB Stuttgart";
		int round = 2;

		Document currentRoundDoc = DocumentUtils.parse(ClasspathUtils.getContentUTF8("crawl-data/bundesliga_post_standing_2.xml"));

		String prevRoundStats = ClasspathUtils.getContentUTF8("crawl-data/bundesliga_team_stats_round_1.xml");
		Map<String, Integer> rankingTable = Bundesliga.createRankingTable(currentRoundDoc);

		String allMatchesFirstTeamContent = ClasspathUtils.getContentUTF8("crawl-data/bayern-munich_2012.html");
		String allMatchesSecondTeamContent = ClasspathUtils.getContentUTF8("crawl-data/vfb-stuttgart_2012.html");

		Map<String, Integer> avgStatsPrev =
				Bundesliga.parseAverageRoundStats(ClasspathUtils.getContentUTF8("crawl-data/bundesliga_team_stats_round_1.xml"));

		LinkedHashMap<String, Object> firstTeamMDATA = new LinkedHashMap<>();
		firstTeamMDATA.put("1", rankingTable.get(firstTeam));
		firstTeamMDATA.put("2", Bundesliga.parsePoints(currentRoundDoc.getElementsByTagName("team").item(0)));
		firstTeamMDATA.put("3", Bundesliga.parseGoalDifference(currentRoundDoc.getElementsByTagName("team").item(0)));
		firstTeamMDATA.put("4", Bundesliga.parseTeamPerformance(prevRoundStats, firstTeam, avgStatsPrev));
		firstTeamMDATA.put("5", ResultDB.parseLastFiveGamesForTeam(allMatchesFirstTeamContent, round));

		LinkedHashMap<String, Object> secondTeamMDATA = new LinkedHashMap<>();
		secondTeamMDATA.put("1", rankingTable.get(secondTeam));
		secondTeamMDATA.put("2", Bundesliga.parsePoints(currentRoundDoc.getElementsByTagName("team").item(17)));
		secondTeamMDATA.put("3", Bundesliga.parseGoalDifference(currentRoundDoc.getElementsByTagName("team").item(17)));
		secondTeamMDATA.put("4", Bundesliga.parseTeamPerformance(prevRoundStats, secondTeam, avgStatsPrev));
		secondTeamMDATA.put("5", ResultDB.parseLastFiveGamesForTeam(allMatchesSecondTeamContent, round));

		secondTeamMDATA.put("6", ResultDB.parseMatchResult(round, allMatchesFirstTeamContent));

		FootballMatch m = new FootballMatch();
		m.setRound(round);
		MatchMetaData mmd = new MatchMetaData();
		mmd.setFirstTeamMetaData(firstTeamMDATA);
		mmd.setSecondTeamMetaData(secondTeamMDATA);
		m.setMatchMetaData(mmd);

		String expected = "2,1,6,8,103267,104,571,18,12,1,0,0,0,0,18,0,-6,120981,211,432,10,15,0,0,0,1,0,6-1";

		Assert.assertEquals(expected, m.getMetaDataNNInput());
	}
}
