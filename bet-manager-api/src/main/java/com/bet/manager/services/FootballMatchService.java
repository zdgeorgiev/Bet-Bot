package com.bet.manager.services;

import com.bet.manager.exceptions.FootballMatchAlreadyExistException;
import com.bet.manager.exceptions.FootballMatchNotFoundExceptions;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.PredictionType;
import com.bet.manager.model.repository.FootballMatchRepository;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

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

	public List<FootballMatch> retrieveMatches(String team1, String team2, Optional<Integer> year, Optional<Integer> round,
			String predictionType, Optional<Boolean> finished, int limit, int offset) {

		List<FootballMatch> filtered = footballMatchRepository.findAll();

		// This kind of filtering is so bad and slow, but JPA ?! no dynamic queries ?!
		filtered = filterByTeams(filtered, team1, team2);
		filtered = filterByYear(filtered, year);
		filtered = filterByRound(filtered, round);
		filtered = filterByPredictionType(filtered, predictionType);
		filtered = filterByFinished(filtered, finished);

		return filtered.stream()
				.sorted(comparing(FootballMatch::getYear).thenComparing(FootballMatch::getRound).reversed())
				.skip(offset)
				.limit(limit)
				.collect(Collectors.toList());
	}

	private List<FootballMatch> filterByTeams(List<FootballMatch> matches, String team1, String team2) {

		List<FootballMatch> result;
		result = filterByTeam(matches, team1);
		result = filterByTeam(result, team2);
		return result;
	}

	private List<FootballMatch> filterByTeam(List<FootballMatch> matches, String team) {

		if (StringUtils.isBlank(team))
			return matches;

		List<FootballMatch> result = new ArrayList<>();

		if (!StringUtils.isBlank(team)) {
			result = matches.stream()
					.filter(m -> m.getHomeTeam().equals(team) || m.getAwayTeam().equals(team))
					.collect(Collectors.toList());
		}

		return result;
	}

	private List<FootballMatch> filterByYear(List<FootballMatch> filtered, Optional<Integer> year) {

		if (!year.isPresent())
			return filtered;

		return filtered.stream()
				.filter(m1 -> m1.getYear() == year.get())
				.collect(Collectors.toList());
	}

	private List<FootballMatch> filterByRound(List<FootballMatch> filtered, Optional<Integer> round) {

		if (!round.isPresent())
			return filtered;

		return filtered.stream()
				.filter(m1 -> m1.getRound() == round.get())
				.collect(Collectors.toList());
	}

	private List<FootballMatch> filterByPredictionType(List<FootballMatch> filtered, String predictionType) {

		// If we get empty prediction type we will skip all the PredictionType.NOT_PREDICTED
		List<PredictionType> predictionTypes = Arrays.asList(PredictionType.CORRECT, PredictionType.INCORRECT);

		return filtered.stream()
				.filter(m -> !StringUtils.isBlank(predictionType) ?
						m.getPredictionType().equals(PredictionType.valueOf(predictionType)) :
						predictionTypes.contains(m.getPredictionType()))
				.collect(Collectors.toList());
	}

	private List<FootballMatch> filterByFinished(List<FootballMatch> filtered, Optional<Boolean> finished) {

		if (!finished.isPresent())
			return filtered;

		return filtered.stream()
				.filter(m1 -> m1.isFinished() == finished.get())
				.collect(Collectors.toList());
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
