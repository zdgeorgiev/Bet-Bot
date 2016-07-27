package com.bet.manager.services;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.exceptions.FootballMatchAlreadyExistException;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.repository.FootballMatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class FootballMatchService {

	private static Logger log = LoggerFactory.getLogger(FootballMatchService.class);

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	public int createMatches(List<FootballMatch> matches) {

		int successfullyCreated = 0;

		for (FootballMatch match : matches) {
			try {

				if (exist(match))
					throw new FootballMatchAlreadyExistException(
							String.format("Football Match '%s' already exist", match.getSummary()));

				footballMatchRepository.save(match);
				successfullyCreated++;

			} catch (Exception e) {
				log.warn("Failed to save football match in the database", e);
			}
		}

		return successfullyCreated;
	}

	private boolean exist(FootballMatch match) {
		return footballMatchRepository
				.findByHomeTeamAndAwayTeamAndStartDate(match.getHomeTeam(), match.getAwayTeam(), match.getStartDate()) != null;
	}

	public Collection<FootballMatch> retrieveAll() {
		return footballMatchRepository.findAll();
	}

	public Page<FootballMatch> retrieveMatches(String team1, String team2, Optional<Integer> year, Optional<Integer> round,
			boolean correctPrediction, boolean finished, int limit, int offset) {

		// TODO:
		return null;
	}

	public int updateMatch(List<FootballMatch> match) {

		// TODO:
		return 0;
	}

	public int correctPredictedMatchesCount() {
		return footballMatchRepository.findByWinnerNotAndCorrectlyPredicted(ResultMessages.UNKNOWN_WINNER, true).size();
	}

	public int incorrectPredictedMatchesCount() {
		return footballMatchRepository.findByWinnerNotAndCorrectlyPredicted(ResultMessages.UNKNOWN_WINNER, false).size();
	}

	public int matchesCount() {
		return (int) footballMatchRepository.count();
	}

	public void deleteAll() {
		footballMatchRepository.deleteAll();
	}
}
