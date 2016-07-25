package com.bet.manager.services;

import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.repository.MatchMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MatchMetaDataService {

	@Autowired
	private MatchMetaDataRepository matchMetaDataRepository;

	public void createEntry(MatchMetaData entry) {
		matchMetaDataRepository.save(entry);
	}

	public int createEntries(List<MatchMetaData> entries) {

		int successfullyCreated = 0;

		for (MatchMetaData matchMetaData : entries) {
			try {
				createEntry(matchMetaData);
				successfullyCreated++;
			} catch (Exception e) {
				throw new RuntimeException(); // TODO : FIX THIS
			}
		}

		return successfullyCreated;
	}

	public Collection<MatchMetaData> retrieveMetaData(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round, int limit, int offset) {
		return matchMetaDataRepository.findByHomeTeam(homeTeam);
	}

	public Collection<MatchMetaData> retrieveAll() {
		return matchMetaDataRepository.findAll();
	}

	public void deleteAll() {
		matchMetaDataRepository.deleteAll();
	}

	public int metaDataCount() {
		return (int) matchMetaDataRepository.count();
	}
}
