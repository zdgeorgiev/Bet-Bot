package com.bet.manager.core;

import java.util.HashMap;
import java.util.Map;

public class TeamsMapping {

	public static Map<Integer, String> bundesligaIdToName;
	public static Map<String, String> resultDBToBundesliga;
	public static Map<String, String> bundesligaToResultDB;
	public static Map<String, String> footballDataToBundesliga;

	private TeamsMapping() {
	}

	static {
		bundesligaIdToName = new HashMap<>();
		resultDBToBundesliga = new HashMap<>();
		bundesligaToResultDB = new HashMap<>();
		footballDataToBundesliga = new HashMap<>();

		bundesligaIdToName.put(18, "Borussia Dortmund");
		bundesligaIdToName.put(10, "FC Bayern München");
		bundesligaIdToName.put(56, "VfL Wolfsburg");
		bundesligaIdToName.put(15, "1.FC Köln");
		bundesligaIdToName.put(36, "1.FSV Mainz 05");
		bundesligaIdToName.put(16, "Bayer 04 Leverkusen");
		bundesligaIdToName.put(12, "Eintracht Frankfurt");
		bundesligaIdToName.put(24, "FC Schalke 04");
		bundesligaIdToName.put(43, "Hertha BSC");
		bundesligaIdToName.put(11, "SV Werder Bremen");
		bundesligaIdToName.put(13, "Hamburger SV");
		bundesligaIdToName.put(3084, "1899 Hoffenheim");
		bundesligaIdToName.put(267, "FC Augsburg");
		bundesligaIdToName.put(44, "Hannover 96");
		bundesligaIdToName.put(14, "VfB Stuttgart");
		bundesligaIdToName.put(17, "Borussia M'gladbach");
		bundesligaIdToName.put(33, "SC Freiburg");
		bundesligaIdToName.put(22, "1.FC Nürnberg");
		bundesligaIdToName.put(42, "FC St. Pauli");
		bundesligaIdToName.put(51, "Eintracht Braunschweig");
		bundesligaIdToName.put(21, "VfL Bochum");
		bundesligaIdToName.put(9, "1.FC Kaiserslautern");
		bundesligaIdToName.put(223, "Arminia Bielefeld");
		bundesligaIdToName.put(27, "Fortuna Düsseldorf");
		bundesligaIdToName.put(2321, "SC Paderborn 07");
		bundesligaIdToName.put(1649, "SpVgg Greuther Fürth");
		bundesligaIdToName.put(34, "SV Darmstadt 98");
		bundesligaIdToName.put(5100, "RB Leipzig");
		bundesligaIdToName.put(4158, "FC Ingolstadt 04");

		resultDBToBundesliga.put("Borussia Dortmund", "Borussia Dortmund");
		resultDBToBundesliga.put("Bayern Munich", "FC Bayern München");
		resultDBToBundesliga.put("VFL Wolfsburg", "VfL Wolfsburg");
		resultDBToBundesliga.put("FC Köln", "1.FC Köln");
		resultDBToBundesliga.put("FSV Mainz 05", "1.FSV Mainz 05");
		resultDBToBundesliga.put("Bayer Leverkusen", "Bayer 04 Leverkusen");
		resultDBToBundesliga.put("Eintracht Frankfurt", "Eintracht Frankfurt");
		resultDBToBundesliga.put("Schalke 04", "FC Schalke 04");
		resultDBToBundesliga.put("Hertha Berlin", "Hertha BSC");
		resultDBToBundesliga.put("Werder Bremen", "SV Werder Bremen");
		resultDBToBundesliga.put("Hamburger SV", "Hamburger SV");
		resultDBToBundesliga.put("1899 Hoffenheim", "1899 Hoffenheim");
		resultDBToBundesliga.put("Augsburg", "FC Augsburg");
		resultDBToBundesliga.put("Hannover 96", "Hannover 96");
		resultDBToBundesliga.put("VFB Stuttgart", "VfB Stuttgart");
		resultDBToBundesliga.put("Mönchengladbach", "Borussia M'gladbach");
		resultDBToBundesliga.put("SC Freiburg", "SC Freiburg");
		resultDBToBundesliga.put("Nürnberg", "1.FC Nürnberg");
		resultDBToBundesliga.put("St. Pauli", "FC St. Pauli");
		resultDBToBundesliga.put("Eintracht Braunschweig", "Eintracht Braunschweig");
		resultDBToBundesliga.put("VFL Bochum", "VfL Bochum");
		resultDBToBundesliga.put("Kaiserslautern", "1.FC Kaiserslautern");
		resultDBToBundesliga.put("Arminia Bielefeld", "Arminia Bielefeld");
		resultDBToBundesliga.put("Fortuna Düsseldorf", "Fortuna Düsseldorf");
		resultDBToBundesliga.put("Greuther Fürth", "SpVgg Greuther Fürth");
		resultDBToBundesliga.put("Paderborn 07", "SC Paderborn 07");
		resultDBToBundesliga.put("darmstadt-98", "SV Darmstadt 98");
		bundesligaToResultDB.put("ingolstadt", "Ingolstadt");
		//		bundesligaToResultDB.put("leipzig", "RB Leipzig");

		bundesligaToResultDB.put("Borussia Dortmund", "borussia-dortmund");
		bundesligaToResultDB.put("FC Bayern München", "bayern-munich");
		bundesligaToResultDB.put("VfL Wolfsburg", "vfl-wolfsburg");
		bundesligaToResultDB.put("1.FC Köln", "fc-köln");
		bundesligaToResultDB.put("1.FSV Mainz 05", "fsv-mainz-05");
		bundesligaToResultDB.put("Bayer 04 Leverkusen", "bayer-leverkusen");
		bundesligaToResultDB.put("Eintracht Frankfurt", "eintracht-frankfurt");
		bundesligaToResultDB.put("FC Schalke 04", "schalke-04");
		bundesligaToResultDB.put("Hertha BSC", "hertha-berlin");
		bundesligaToResultDB.put("SV Werder Bremen", "werder-bremen");
		bundesligaToResultDB.put("Hamburger SV", "hamburger-sv");
		bundesligaToResultDB.put("1899 Hoffenheim", "1899-hoffenheim");
		bundesligaToResultDB.put("FC Augsburg", "augsburg");
		bundesligaToResultDB.put("Hannover 96", "hannover-96");
		bundesligaToResultDB.put("VfB Stuttgart", "vfb-stuttgart");
		bundesligaToResultDB.put("Borussia M'gladbach", "mönchengladbach");
		bundesligaToResultDB.put("SC Freiburg", "sc-freiburg");
		bundesligaToResultDB.put("1.FC Nürnberg", "nürnberg");
		bundesligaToResultDB.put("FC St. Pauli", "st.-pauli");
		bundesligaToResultDB.put("Eintracht Braunschweig", "eintracht-braunschweig");
		bundesligaToResultDB.put("VfL Bochum", "vfl-bochum");
		bundesligaToResultDB.put("1.FC Kaiserslautern", "kaiserslautern");
		bundesligaToResultDB.put("Arminia Bielefeld", "arminia-bielefeld");
		bundesligaToResultDB.put("Fortuna Düsseldorf", "fortuna-düsseldorf");
		bundesligaToResultDB.put("SC Paderborn 07", "Paderborn 07");
		bundesligaToResultDB.put("SpVgg Greuther Fürth", "Greuther Fürth");
		bundesligaToResultDB.put("SV Darmstadt 98", "darmstadt-98");
		bundesligaToResultDB.put("Ingolstadt", "ingolstadt");
		//		bundesligaToResultDB.put("RB Leipzig", "leipzig");

		footballDataToBundesliga.put("Borussia Dortmund", "Borussia Dortmund");
		footballDataToBundesliga.put("FC Bayern München", "FC Bayern München");
		footballDataToBundesliga.put("Bor. Mönchengladbach", "Borussia M'gladbach");
		footballDataToBundesliga.put("FC Augsburg", "FC Augsburg");
		footballDataToBundesliga.put("SV Darmstadt 98", "SV Darmstadt 98");
		footballDataToBundesliga.put("Eintracht Frankfurt", "Eintracht Frankfurt");
		footballDataToBundesliga.put("SC Freiburg", "SC Freiburg");
		footballDataToBundesliga.put("Hamburger SV", "Hamburger SV");
		footballDataToBundesliga.put("Hertha BSC", "Hertha BSC");
		footballDataToBundesliga.put("TSG 1899 Hoffenheim", "1899 Hoffenheim");
		footballDataToBundesliga.put("1. FSV Mainz 05", "1.FSV Mainz 05");
		footballDataToBundesliga.put("FC Schalke 04", "FC Schalke 04");
		footballDataToBundesliga.put("Werder Bremen", "SV Werder Bremen");
		footballDataToBundesliga.put("VfL Wolfsburg", "VfL Wolfsburg");
		footballDataToBundesliga.put("1. FC Köln", "1.FC Köln");
		footballDataToBundesliga.put("Ingolstadt", "Ingolstadt");
		footballDataToBundesliga.put("Bayer Leverkusen", "Bayer 04 Leverkusen");
		footballDataToBundesliga.put("Hannover 96", "Hannover 96");
		footballDataToBundesliga.put("VfB Stuttgart", "VfB Stuttgart");
		footballDataToBundesliga.put("Red Bull Leipzig", "RB Leipzig");
		footballDataToBundesliga.put("FC Ingolstadt 04", "ingolstadt");

		footballDataToBundesliga.put("FC Köln", "1.FC Köln");
		footballDataToBundesliga.put("Nürnberg", "1.FC Nürnberg");
		footballDataToBundesliga.put("St. Pauli", "FC St. Pauli");
		footballDataToBundesliga.put("Eintracht Braunschweig", "Eintracht Braunschweig");
		footballDataToBundesliga.put("VFL Bochum", "VfL Bochum");
		footballDataToBundesliga.put("Kaiserslautern", "1.FC Kaiserslautern");
		footballDataToBundesliga.put("Arminia Bielefeld", "Arminia Bielefeld");
		footballDataToBundesliga.put("Fortuna Düsseldorf", "Fortuna Düsseldorf");
		footballDataToBundesliga.put("Greuther Fürth", "SpVgg Greuther Fürth");
		footballDataToBundesliga.put("Paderborn 07", "SC Paderborn 07");

	}
}
