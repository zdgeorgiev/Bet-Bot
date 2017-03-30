package com.bet.manager.core.data.sources;

import org.apache.commons.collections.MapUtils;

import java.net.URL;
import java.util.Map;

public class Espnfc {

	private Espnfc() {

	}

	public static String getTeamOpponent(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public static String parseTeamOpponent(String allMatchesHTML, int round) {
		return null;
	}

	public static String getMatchResult(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return null;
	}

	public static String parseMatchResult(int round, String allMatchesHTML) {
		return null;
	}

	public static Map<String, Integer> getLastFiveGames(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return MapUtils.EMPTY_MAP;
	}

	public static String parseLastFiveGamesForTeam(String allMatchesHTML, int round) {
		return null;
	}

	public static String getMatchVenue(String bundesLigaTeam, int year, int round, Map<URL, String> crawledPages)
			throws Exception {
		return "-1";
	}

	public static String parseMatchVenue(String allMatchesHTML, int round) {
		return null;
	}

	public static void addMatchToNormalizationArray(String result, String score, int[] matchesNormalization) {

	}
}
