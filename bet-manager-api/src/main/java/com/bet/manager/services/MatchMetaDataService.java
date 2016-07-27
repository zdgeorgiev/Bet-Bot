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

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MatchMetaDataService {

	private static Logger log = LoggerFactory.getLogger(MatchMetaDataService.class);

	private static final int MIN_YEAR = 2011;
	private static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR);

	private static final int MIN_ROUND = 2;
	private static final int MAX_ROUND = 34;

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

	private boolean exist(MatchMetaData matchMetaData) {
		return matchMetaDataRepository
				.findByHomeTeamAndAwayTeamAndYearAndRound(matchMetaData.getHomeTeam(), matchMetaData.getAwayTeam(),
						matchMetaData.getYear(), matchMetaData.getRound()) != null;
	}

	// TODO : This "homeTeam" and "awayTeam" should be renamed to "firstTeam" and "secondTeam" and
	// TODO : create query which if only 1 team is presented then should look at metadata with this team presented
	// TODO : not only as home team or away team but both
	public Page<MatchMetaData> retrieveMetaData(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round, int limit, int offset) {

		if (year.isPresent())
			checkYear(year.get());
		if (round.isPresent())
			checkRound(round.get());

		Pageable pageable = new PageRequest(offset, limit, Sort.Direction.DESC, "year", "round");
		//TODO : Improve the search using the away team, year and round as part of the query
		Page<MatchMetaData> matchMetaData = matchMetaDataRepository.findByHomeTeam(homeTeam, pageable);

		if (matchMetaData == null || matchMetaData.getNumberOfElements() == 0)
			throw notFoundError(homeTeam, awayTeam, year, round);

		return matchMetaData;
	}

	private void checkYear(Integer year) {
		if (year < MIN_YEAR || year > MAX_YEAR)
			throw new IllegalArgumentException(String.format("Year cant be less than %s or later than %s", MIN_YEAR, MAX_YEAR));
	}

	private void checkRound(Integer round) {
		if (round < MIN_ROUND || round > MAX_ROUND)
			throw new IllegalArgumentException(String.format("Round cant be less than %s or later than %s", MIN_ROUND, MAX_ROUND));
	}

	private MatchMetaDataNotFoundException notFoundError(String homeTeam, String awayTeam, Optional<Integer> year,
			Optional<Integer> round) {
		StringBuilder errorResponse = new StringBuilder();

		errorResponse.append(String.format("Match metadata for [%s]", homeTeam));

		if (!StringUtil.isBlank(awayTeam))
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
}
