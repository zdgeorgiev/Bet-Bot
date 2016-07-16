package com.bet.manager.services;

import com.bet.manager.web.model.MatchMetaData;
import com.bet.manager.web.model.PreviousRoundStats;
import com.bet.manager.web.model.TeamMetaData;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class MatchMetaDataService {

	public void createEntry(MatchMetaData entry) {
	}

	public int createEntries(List<MatchMetaData> entries) {
		return 0;
	}

	public Collection<MatchMetaData> retrieveMetaData(String team, String opponent, Integer year1, Integer i, int year, int round) {
		return Arrays.asList(new MatchMetaData("a", -1, -5,
				new TeamMetaData(1, 2, 3, new PreviousRoundStats(4, 5, 6, 7, 8)),
				new TeamMetaData(1, 2, 3, new PreviousRoundStats(4, 5, 6, 7, 8))));
	}

	public void deleteAll() {

	}

	public int metaDataCount() {
		return 0;
	}
}
