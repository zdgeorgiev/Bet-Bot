package com.bet.manager.services;

import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.models.util.FootballMatchBuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class MatchMetaDataService {

	public void createEntry(MatchMetaData entry) {

	}

	public int createEntries(List<MatchMetaData> entries) {

		return 0;
	}

	public Collection<MatchMetaData> retrieveMetaData(String team, String opponent, Integer year1, Integer i, int year, int round) {
		return Arrays.asList(new MatchMetaData());
	}

	public void deleteAll() {

	}

	public int metaDataCount() {
		return 0;
	}

	public FootballMatch test() {

		return new FootballMatchBuilder().setHomeTeamName("Chelsea").setAwayTeamName("Arsenal").setStartDate(new Date()).build();
	}
}
