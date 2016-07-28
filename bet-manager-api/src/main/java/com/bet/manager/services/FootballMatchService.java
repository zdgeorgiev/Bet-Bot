package com.bet.manager.services;

import com.bet.manager.exceptions.FootballMatchAlreadyExistException;
import com.bet.manager.exceptions.FootballMatchNotFoundExceptions;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.PredictionType;
import com.bet.manager.model.repository.FootballMatchRepository;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
		return retrieve(match) != null;
	}

	private FootballMatch retrieve(FootballMatch match) {
		return footballMatchRepository
				.findByHomeTeamAndAwayTeamAndYearAndRound(match.getHomeTeam(), match.getAwayTeam(), match.getYear(),
						match.getRound());
	}

	// TODO : ONLY FOR TEST ( ATLEAST ADD PAGINATION )
	public Collection<FootballMatch> retrieveAll() {
		return footballMatchRepository.findAll();
	}

	public Page<FootballMatch> retrieveMatches(String team1, String team2, Optional<Integer> year, Optional<Integer> round,
			Optional<Boolean> correctPrediction, Optional<Boolean> finished, int limit, int offset) {

		// TODO:
		return null;
	}

	public List<FootballMatch> updateMatches(List<FootballMatch> matches) {

		List<FootballMatch> updatedMatches = new ArrayList<>(matches.size());

		for (FootballMatch match : matches) {
			if (!exist(match))
				throw new FootballMatchNotFoundExceptions(
						String.format("Football match %s doesnt exist in th e db", match.getSummary()));

			// Retrieve the match from the data base
			FootballMatch retrievedMatch = retrieve(match);

			// Don't perform update if the retrieved match have result already
			if (retrievedMatch.isFinished()) {
				log.warn("The match {} in the data base is considered already finished. No changes will apply",
						retrievedMatch.getSummary());
				continue;
			}

			FootballMatch updated = new FootballMatchBuilder(retrievedMatch)
					.setResult(match.getResult())
					.build();

			// Not so good performance
			footballMatchRepository.delete(match);
			footballMatchRepository.save(updated);
			updatedMatches.add(updated);
		}

		return updatedMatches;
	}

	public int correctPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndFinishedTrue(PredictionType.CORRECT).size();
	}

	public int incorrectPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndFinishedTrue(PredictionType.INCORRECT).size();
	}

	public int matchesCount() {
		return (int) footballMatchRepository.count();
	}

	public void deleteAll() {
		footballMatchRepository.deleteAll();
	}

}
