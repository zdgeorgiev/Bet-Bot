package com.bet.manager.services;

import com.bet.manager.web.model.MatchMetaData;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MatchMetaDataService {

	public void createEntry(MatchMetaData entry) {
	}

	public int createEntries(List<MatchMetaData> entries) {
		return 0;
	}

	public Collection<MatchMetaData> retrieveMetaData(String team, String opponent, int year1, int i, int year, int round) {
		return null;
	}

	public void deleteAll() {

	}

	public int metaDataCount() {
		return 0;
	}
}
