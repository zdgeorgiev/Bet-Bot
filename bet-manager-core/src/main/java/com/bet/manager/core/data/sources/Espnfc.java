package com.bet.manager.core.data.sources;

import org.apache.commons.collections.MapUtils;

import java.net.URL;
import java.util.Map;

public class Espnfc {

	public String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public String parseTeamOpponent(String allMatchesHTML, int round) {
		return null;
	}

	public String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public String parseMatchResult(int round, String allMatchesHTML) {
		return null;
	}

	public Map<String, Integer> getLastFiveGames(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return MapUtils.EMPTY_MAP;
	}

	public String parseLastFiveGamesForTeam(String allMatchesHTML, int round) {
		return null;
	}

	public String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return "-1";
	}

	public String parseMatchVenue(String allMatchesHTML, int round) {
		return null;
	}

	public void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization) {

	}
}
