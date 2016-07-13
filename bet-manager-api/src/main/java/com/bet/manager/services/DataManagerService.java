package com.bet.manager.services;

import com.bet.manager.core.data.DataManager;
import org.springframework.stereotype.Service;

@Service
public class DataManagerService {

	private DataManager dataManager;

	public DataManagerService() {
		// TODO: This should be set to true
		this.dataManager = new DataManager(false);
	}

	public String getDataForMatch(String team1, String team2, int year, int round) {

		// TODO: VALIDATION!!

		String result = "";

		try {
			result = dataManager.getDataForMatch(team1, team2, year, round);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
