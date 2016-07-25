package com.bet.manager.services;

import com.bet.manager.exceptions.FailedToSaveMatchMetaDataException;
import com.bet.manager.exceptions.MatchMetaDataAlreadyExistException;
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

	public int createEntries(List<MatchMetaData> entries) {

		int successfullyCreated = 0;

		for (MatchMetaData matchMetaData : entries) {
			try {

				if (matchMetaDataRepository.findByLabel(matchMetaData.toString()) != null)
					throw new MatchMetaDataAlreadyExistException(
							String.format("Match metadata '%s' already exist", matchMetaData.toString()));

				matchMetaDataRepository.save(matchMetaData);
				successfullyCreated++;

			} catch (Exception e) {
				throw new FailedToSaveMatchMetaDataException(
						String.format("Cannot create '%s' match metadata.", matchMetaData.toString()), e);
			}
		}

		return successfullyCreated;
	}

	public Collection<MatchMetaData> retrieveMetaData(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round, int limit, int offset) {
		// TODO: Fix this
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
