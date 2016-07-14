package com.bet.manager.services;

import com.bet.manager.core.data.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataManagerService {

	@Autowired
	private DataManager dataManager;

	public String getDataForMatch(String team1, String team2, int year, int round) throws Exception {

		return dataManager.getDataForMatch(team1, team2, year, round);
	}
}
