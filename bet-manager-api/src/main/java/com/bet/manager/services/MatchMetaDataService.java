package com.bet.manager.services;

import com.bet.manager.models.dao.MatchMetaData;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MatchMetaDataService {

	public void createEntry(MatchMetaData entry) {

	}

	public int createEntries(List<MatchMetaData> entries) {

		return 0;
	}

	public Collection<MatchMetaData> retrieveMetaData(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round, int limit, int offset) {
		return Arrays.asList(new MatchMetaData());
	}

	public void deleteAll() {

	}

	public int metaDataCount() {
		return 0;
	}
}
