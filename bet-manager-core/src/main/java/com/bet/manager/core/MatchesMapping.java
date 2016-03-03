package com.bet.manager.core;

import java.util.HashMap;
import java.util.Map;

public class MatchesMapping {

	public static Map<Integer, String> bundesligaIdToName;
	public static Map<String, String> resultDBToBundesliga;
	public static Map<String, String> bundesligaToResultDB;

	private MatchesMapping() {
	}

	static {
		bundesligaIdToName = new HashMap<>();
		resultDBToBundesliga = new HashMap<>();
		bundesligaToResultDB = new HashMap<>();

		bundesligaIdToName.put(18, "Borussia Dortmund");
		bundesligaIdToName.put(10, "FC Bayern München");
		bundesligaIdToName.put(56, "VfL Wolfsburg");
		bundesligaIdToName.put(15, "1.FC Köln");
		bundesligaIdToName.put(36, "1.FSV Mainz 05");
		bundesligaIdToName.put(16, "Bayer 04 Leverkusen");
		bundesligaIdToName.put(4158, "FC Ingolstadt 04");
		bundesligaIdToName.put(12, "Eintracht Frankfurt");
		bundesligaIdToName.put(24, "FC Schalke 04");
		bundesligaIdToName.put(43, "Hertha BSC");
		bundesligaIdToName.put(11, "SV Werder Bremen");
		bundesligaIdToName.put(34, "SV Darmstadt 98");
		bundesligaIdToName.put(13, "Hamburger SV");
		bundesligaIdToName.put(3084, "1899 Hoffenheim");
		bundesligaIdToName.put(267, "FC Augsburg");
		bundesligaIdToName.put(44, "Hannover 96");
		bundesligaIdToName.put(14, "VfB Stuttgart");
		bundesligaIdToName.put(17, "Borussia M'gladbach");
		//bundesligaIdToName.put(5100, "RB Leipzig");
		bundesligaIdToName.put(33, "SC Freiburg");
		bundesligaIdToName.put(22, "1.FC Nürnberg");
		bundesligaIdToName.put(42, "FC St. Pauli");
		bundesligaIdToName.put(51, "Eintracht Braunschweig");
		bundesligaIdToName.put(21, "VfL Bochum");
		//bundesligaIdToName.put(1100, "SV Sandhausen");
		bundesligaIdToName.put(9, "1.FC Kaiserslautern");
		//bundesligaIdToName.put(20,   "Karlsruher SC");
		//bundesligaIdToName.put(1649, "SpVgg Greuther Fürth");
		//bundesligaIdToName.put(4936, "1.FC Heidenheim");
		bundesligaIdToName.put(223, "Arminia Bielefeld");
		//bundesligaIdToName.put(297,  "1.FC Union Berlin");
		//bundesligaIdToName.put(277,  "FSV Frankfurt");
		bundesligaIdToName.put(27, "Fortuna Düsseldorf");
		//bundesligaIdToName.put(2321, "SC Paderborn 07");
		//bundesligaIdToName.put(35,   "TSV 1860 München");
		//bundesligaIdToName.put(29,   "MSV Duisburg");

		resultDBToBundesliga.put("Borussia Dortmund", "Borussia Dortmund");
		resultDBToBundesliga.put("Bayern Munich", "FC Bayern München");
		resultDBToBundesliga.put("VFL Wolfsburg", "VfL Wolfsburg");
		resultDBToBundesliga.put("FC Köln", "1.FC Köln");
		resultDBToBundesliga.put("FSV Mainz 05", "1.FSV Mainz 05");
		resultDBToBundesliga.put("Bayer Leverkusen", "Bayer 04 Leverkusen");
		resultDBToBundesliga.put("Ingolstadt", "FC Ingolstadt 04");
		resultDBToBundesliga.put("Eintracht Frankfurt", "Eintracht Frankfurt");
		resultDBToBundesliga.put("Schalke 04", "FC Schalke 04");
		resultDBToBundesliga.put("Hertha Berlin", "Hertha BSC");
		resultDBToBundesliga.put("Werder Bremen", "SV Werder Bremen");
		resultDBToBundesliga.put("Darmstadt 98", "SV Darmstadt 98");
		resultDBToBundesliga.put("Hamburger SV", "Hamburger SV");
		resultDBToBundesliga.put("1899 Hoffenheim", "1899 Hoffenheim");
		resultDBToBundesliga.put("Augsburg", "FC Augsburg");
		resultDBToBundesliga.put("Hannover 96", "Hannover 96");
		resultDBToBundesliga.put("VFB Stuttgart", "VfB Stuttgart");
		resultDBToBundesliga.put("Mönchengladbach", "Borussia M'gladbach");
		//resultDBToBundesliga.put("RB Leipzig");
		resultDBToBundesliga.put("SC Freiburg", "SC Freiburg");
		resultDBToBundesliga.put("Nürnberg", "1.FC Nürnberg");
		resultDBToBundesliga.put("St. Pauli", "FC St. Pauli");
		resultDBToBundesliga.put("Eintracht Braunschweig", "Eintracht Braunschweig");
		resultDBToBundesliga.put("VFL Bochum", "VfL Bochum");
		//resultDBToBundesliga.put("SV Sandhausen");
		resultDBToBundesliga.put("Kaiserslautern", "1.FC Kaiserslautern");
		//resultDBToBundesliga.put("Karlsruher SC");
		//resultDBToBundesliga.put("SpVgg Greuther Fürth");
		//resultDBToBundesliga.put("1.FC Heidenheim");
		resultDBToBundesliga.put("Arminia Bielefeld", "Arminia Bielefeld");
		//resultDBToBundesliga.put("1.FC Union Berlin");
		//resultDBToBundesliga.put("FSV Frankfurt");
		resultDBToBundesliga.put("Fortuna Düsseldorf", "Fortuna Düsseldorf");
		//resultDBToBundesliga.put("SC Paderborn 07");
		//resultDBToBundesliga.put("TSV 1860 München");
		//resultDBToBundesliga.put("MSV Duisburg");

		bundesligaToResultDB.put("Borussia Dortmund", "borussia-dortmund");
		bundesligaToResultDB.put("FC Bayern München", "bayern-munich");
		bundesligaToResultDB.put("VfL Wolfsburg", "vfl-wolfsburg");
		bundesligaToResultDB.put("1.FC Köln", "fc-köln");
		bundesligaToResultDB.put("1.FSV Mainz 05", "fsv-mainz-05");
		bundesligaToResultDB.put("Bayer 04 Leverkusen", "bayer-leverkusen");
		bundesligaToResultDB.put("FC Ingolstadt 04", "ingolstadt");
		bundesligaToResultDB.put("Eintracht Frankfurt", "eintracht-frankfurt");
		bundesligaToResultDB.put("FC Schalke 04", "schalke-04");
		bundesligaToResultDB.put("Hertha BSC", "hertha-berlin");
		bundesligaToResultDB.put("SV Werder Bremen", "werder-bremen");
		bundesligaToResultDB.put("SV Darmstadt 98", "darmstadt-98");
		bundesligaToResultDB.put("Hamburger SV", "hamburger-sv");
		bundesligaToResultDB.put("1899 Hoffenheim", "1899-hoffenheim");
		bundesligaToResultDB.put("FC Augsburg", "augsburg");
		bundesligaToResultDB.put("Hannover 96", "hannover-96");
		bundesligaToResultDB.put("VfB Stuttgart", "vfb-stuttgart");
		bundesligaToResultDB.put("Borussia M'gladbach", "mönchengladbach");
		//bundesligaToResultDB.put("RB Leipzig");
		bundesligaToResultDB.put("SC Freiburg", "sc-freiburg");
		bundesligaToResultDB.put("1.FC Nürnberg", "nürnberg");
		bundesligaToResultDB.put("FC St. Pauli", "st.-pauli");
		bundesligaToResultDB.put("Eintracht Braunschweig", "eintracht-braunschweig");
		bundesligaToResultDB.put("VfL Bochum", "vfl-bochum");
		//bundesligaToResultDB.put("SV Sandhausen");
		bundesligaToResultDB.put("1.FC Kaiserslautern", "kaiserslautern");
		//bundesligaToResultDB.put("Karlsruher SC");
		//bundesligaToResultDB.put("SpVgg Greuther Fürth");
		//bundesligaToResultDB.put("1.FC Heidenheim");
		bundesligaToResultDB.put("Arminia Bielefeld", "arminia-bielefeld");
		//bundesligaToResultDB.put("1.FC Union Berlin");
		//bundesligaToResultDB.put("FSV Frankfurt");
		bundesligaToResultDB.put("Fortuna Düsseldorf", "fortuna-düsseldorf");
		//bundesligaToResultDB.put("SC Paderborn 07");
		//bundesligaToResultDB.put("TSV 1860 München");
		//bundesligaToResultDB.put("MSV Duisburg");
	}
}
