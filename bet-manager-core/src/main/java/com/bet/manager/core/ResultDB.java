package com.bet.manager.core;

import java.util.HashMap;
import java.util.Map;

public class ResultDB {

	public static Map<String, String> bundesLigaMappingToResultDB;

	private ResultDB() {
	}

	static {
		bundesLigaMappingToResultDB = new HashMap<>();

		bundesLigaMappingToResultDB.put("Borussia Dortmund", "borussia-dortmund");
		bundesLigaMappingToResultDB.put("FC Bayern München", "bayern-munich");
		bundesLigaMappingToResultDB.put("VfL Wolfsburg", "vfl-wolfsburg");
		bundesLigaMappingToResultDB.put("1.FC Köln", "fc-köln");
		bundesLigaMappingToResultDB.put("1.FSV Mainz 05", "fsv-mainz-05");
		bundesLigaMappingToResultDB.put("Bayer 04 Leverkusen", "bayer-leverkusen");
		bundesLigaMappingToResultDB.put("FC Ingolstadt 04", "ingolstadt");
		bundesLigaMappingToResultDB.put("Eintracht Frankfurt", "eintracht-frankfurt");
		bundesLigaMappingToResultDB.put("FC Schalke 04", "schalke-04");
		bundesLigaMappingToResultDB.put("Hertha BSC", "hertha-berlin");
		bundesLigaMappingToResultDB.put("SV Werder Bremen", "werder-bremen");
		bundesLigaMappingToResultDB.put("SV Darmstadt 98", "darmstadt-98");
		bundesLigaMappingToResultDB.put("Hamburger SV", "hamburger-sv");
		bundesLigaMappingToResultDB.put("1899 Hoffenheim", "1899-hoffenheim");
		bundesLigaMappingToResultDB.put("FC Augsburg", "augsburg");
		bundesLigaMappingToResultDB.put("Hannover 96", "hannover-96");
		bundesLigaMappingToResultDB.put("VfB Stuttgart", "vfb-stuttgart");
		bundesLigaMappingToResultDB.put("Borussia M'gladbach", "mönchengladbach");
		//bundesLigaMappingToResultDB.put("RB Leipzig");
		bundesLigaMappingToResultDB.put("SC Freiburg", "sc-freiburg");
		bundesLigaMappingToResultDB.put("1.FC Nürnberg", "nürnberg");
		bundesLigaMappingToResultDB.put("FC St. Pauli", "st.-pauli");
		bundesLigaMappingToResultDB.put("Eintracht Braunschweig", "eintracht-braunschweig");
		bundesLigaMappingToResultDB.put("VfL Bochum", "vfl-bochum");
		//bundesLigaMappingToResultDB.put("SV Sandhausen");
		bundesLigaMappingToResultDB.put("1.FC Kaiserslautern", "kaiserslautern");
		//bundesLigaMappingToResultDB.put("Karlsruher SC");
		//bundesLigaMappingToResultDB.put("SpVgg Greuther Fürth");
		//bundesLigaMappingToResultDB.put("1.FC Heidenheim");
		bundesLigaMappingToResultDB.put("Arminia Bielefeld", "arminia-bielefeld");
		//bundesLigaMappingToResultDB.put("1.FC Union Berlin");
		//bundesLigaMappingToResultDB.put("FSV Frankfurt");
		bundesLigaMappingToResultDB.put("Fortuna Düsseldorf", "fortuna-düsseldorf");
		//bundesLigaMappingToResultDB.put("SC Paderborn 07");
		//bundesLigaMappingToResultDB.put("TSV 1860 München");
		//bundesLigaMappingToResultDB.put("MSV Duisburg");
	}
}
