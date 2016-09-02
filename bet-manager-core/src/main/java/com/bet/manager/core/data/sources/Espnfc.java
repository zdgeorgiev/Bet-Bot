package com.bet.manager.core.data.sources;

import java.net.URL;
import java.util.Map;

public class Espnfc implements ISecondarySource {

	@Override public String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public String parseTeamOpponent(String allMatchesHTML, int round) {
		return null;
	}

	@Override public String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public String parseMatchResult(int round, String allMatchesHTML) {
		return null;
	}

	@Override
	public String getLastFiveGamesForTeam(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return "0,0,1,0,2";
	}

	public String parseLastFiveGamesForTeam(String allMatchesHTML, int round) {
		return null;
	}

	@Override public String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return "1";
	}

	public String parseMatchVenue(String allMatchesHTML, int round) {
		return null;
	}

	public void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization) {

	}
}
