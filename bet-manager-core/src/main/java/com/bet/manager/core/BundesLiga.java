package com.bet.manager.core;

import java.util.HashMap;
import java.util.Map;


public class BundesLiga {

	public static Map<Integer, String> teams;

	private BundesLiga() {
	}

	static {
		teams = new HashMap<>();

		teams.put(18,   "Borussia Dortmund");
		teams.put(10,   "FC Bayern München");
		teams.put(56,   "VfL Wolfsburg");
		teams.put(15,   "1.FC Köln");
		teams.put(36,   "1.FSV Mainz 05");
		teams.put(16,   "Bayer 04 Leverkusen");
		teams.put(4158, "FC Ingolstadt 04");
		teams.put(12,   "Eintracht Frankfurt");
		teams.put(24,   "FC Schalke 04");
		teams.put(43,   "Hertha BSC");
		teams.put(11,   "SV Werder Bremen");
		teams.put(34,   "SV Darmstadt 98");
		teams.put(13,   "Hamburger SV");
		teams.put(3084, "1899 Hoffenheim");
		teams.put(267,  "FC Augsburg");
		teams.put(44,   "Hannover 96");
		teams.put(14,   "VfB Stuttgart");
		teams.put(17,   "Borussia M'gladbach");
		//teams.put(5100, "RB Leipzig");
		teams.put(33,   "SC Freiburg");
		teams.put(22,   "1.FC Nürnberg");
		teams.put(42,   "FC St. Pauli");
		teams.put(51,   "Eintracht Braunschweig");
		teams.put(21,   "VfL Bochum");
		//teams.put(1100, "SV Sandhausen");
		teams.put(9,    "1.FC Kaiserslautern");
		//teams.put(20,   "Karlsruher SC");
		//teams.put(1649, "SpVgg Greuther Fürth");
		//teams.put(4936, "1.FC Heidenheim");
		teams.put(223,  "Arminia Bielefeld");
		//teams.put(297,  "1.FC Union Berlin");
		//teams.put(277,  "FSV Frankfurt");
		teams.put(27,   "Fortuna Düsseldorf");
		//teams.put(2321, "SC Paderborn 07");
		//teams.put(35,   "TSV 1860 München");
		//teams.put(29,   "MSV Duisburg");
	}
}
