package com.bet.manager.services;

import com.bet.manager.exceptions.MatchMetaDataAlreadyExistException;
import com.bet.manager.exceptions.MatchMetaDataNotFoundException;
import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.repository.MatchMetaDataRepository;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MatchMetaDataService {

	private static Logger log = LoggerFactory.getLogger(MatchMetaDataService.class);

	@Autowired
	private MatchMetaDataRepository matchMetaDataRepository;

	public int createEntries(List<MatchMetaData> entries) {

		int successfullyCreated = 0;

		for (MatchMetaData matchMetaData : entries) {
			try {

				if (exist(matchMetaData))
					throw new MatchMetaDataAlreadyExistException(
							String.format("Match metadata '%s' already exist", matchMetaData.getSummary()));

				matchMetaDataRepository.save(matchMetaData);
				successfullyCreated++;

			} catch (Exception e) {
				log.warn("Failed to save match metadata in the database", e);
			}
		}

		return successfullyCreated;
	}

	public Page<MatchMetaData> retrieveMetaData(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round, int limit, int offset) {

		Pageable pageable = new PageRequest(offset, limit, Sort.Direction.DESC, "year", "round");
		//TODO : CHANGE THIS
		Page<MatchMetaData> matchMetaData = matchMetaDataRepository.findByHomeTeam(homeTeam, awayTeam, year, round, pageable);

		if (matchMetaData == null || matchMetaData.getNumberOfElements() == 0)
			throw createNotFoundError(homeTeam, awayTeam, year, round);

		return matchMetaData;
	}

	private MatchMetaDataNotFoundException createNotFoundError(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round) {
		StringBuilder errorResponse = new StringBuilder();

		errorResponse.append(String.format("Match metadata for [%s]", homeTeam));

		if (!StringUtil.isBlank(homeTeam))
			errorResponse.append(String.format(" vs [%s]", awayTeam));
		if (year.isPresent())
			errorResponse.append(String.format(" for [%s]", year.get()));
		if (round.isPresent())
			errorResponse.append(String.format(" round [%s]", round.get()));

		errorResponse.append(" not found.");

		throw new MatchMetaDataNotFoundException(errorResponse.toString());
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

	private boolean exist(MatchMetaData matchMetaData) {
		return matchMetaDataRepository
				.findByHomeTeamAndAwayTeamAndYearAndRound(matchMetaData.getHomeTeam(), matchMetaData.getAwayTeam(),
						matchMetaData.getYear(), matchMetaData.getRound()) != null;
	}
}
