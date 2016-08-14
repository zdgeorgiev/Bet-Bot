package com.bet.manager.services;

import com.bet.manager.commons.ResultMessages;
import com.bet.manager.commons.util.LookUpEnumUtils;
import com.bet.manager.exceptions.FootballMatchAlreadyExistException;
import com.bet.manager.exceptions.FootballMatchNotFoundExceptions;
import com.bet.manager.metrics.MetricsCounterContainer;
import com.bet.manager.metrics.SuccessRatioGauge;
import com.bet.manager.metrics.SuccessRatioHealthCheck;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.dao.PredictionType;
import com.bet.manager.model.repository.FootballMatchRepository;
import com.bet.manager.model.util.FootballMatchBuilder;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
public class FootballMatchService {

	private static Logger log = LoggerFactory.getLogger(FootballMatchService.class);

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	@Inject
	private MetricsCounterContainer metricsCounterHolder;

	@Inject
	private MetricRegistry metricRegistry;

	@Inject
	private HealthCheckRegistry healthCheckRegistry;

	@PostConstruct
	public void init() {

		RatioGauge successMatchesRatio = metricRegistry.register(
				MetricRegistry.name(FootballMatchService.class, "success-matches-ratio"),
				new SuccessRatioGauge(metricsCounterHolder.getMatchesSuccess(), metricsCounterHolder.getMatchesFailures()));

		RatioGauge successMetadataRatio = metricRegistry.register(
				MetricRegistry.name(FootballMatchService.class, "success-meta-data-ratio"),
				new SuccessRatioGauge(metricsCounterHolder.getMetadataSuccess(), metricsCounterHolder.getMetadataFailures()));

		RatioGauge successPredictionsRatio = metricRegistry.register(
				MetricRegistry.name(FootballMatchService.class, "success-predictions-ratio"),
				new SuccessRatioGauge(metricsCounterHolder.getPredictionsSuccess(), metricsCounterHolder.getPredictionsFailures()));

		healthCheckRegistry.register("success-matches-ratio-check", new SuccessRatioHealthCheck(successMatchesRatio));
		healthCheckRegistry.register("success-metadata-ratio-check", new SuccessRatioHealthCheck(successMetadataRatio));
		healthCheckRegistry.register("success-predictions-ratio-check", new SuccessRatioHealthCheck(successPredictionsRatio));
	}

	public void createMatches(List<FootballMatch> matches) {

		for (FootballMatch match : matches) {
			try {

				if (exist(match))
					throw new FootballMatchAlreadyExistException(
							String.format("Football Match '%s' already exist", match.getSummary()));

				footballMatchRepository.save(match);
				metricsCounterHolder.incMatchesSuccesses();
				log.info("Successfully created MATCH {}", match.getSummary());

			} catch (Exception e) {
				metricsCounterHolder.incMatchesFailures();
				log.warn("Failed to save football match in the database", e);
			}
		}
	}

	public List<FootballMatch> findAll() {
		return footballMatchRepository.findAll();
	}

	public boolean exist(FootballMatch match) {
		return retrieve(match) != null;
	}

	private FootballMatch retrieve(FootballMatch match) {
		return footballMatchRepository
				.findByHomeTeamAndAwayTeamAndYearAndRound(match.getHomeTeam(), match.getAwayTeam(), match.getYear(),
						match.getRound());
	}

	public List<FootballMatch> retrieveMatches(String team1, String team2, Optional<Integer> year, Optional<Integer> round,
			String predictionType, String matchStatus, int limit, int offset) {

		List<FootballMatch> filtered = footballMatchRepository.findAll();

		// This kind of filtering is so bad and slow, but JPA ?! no dynamic queries ?!
		filtered = filterByTeams(filtered, team1, team2);
		filtered = filterByYear(filtered, year);
		filtered = filterByRound(filtered, round);
		filtered = filterByPredictionType(filtered, predictionType);
		filtered = filterByMatchStatus(filtered, matchStatus);

		return filtered.stream()
				.sorted(comparing(FootballMatch::getYear).reversed().thenComparing(FootballMatch::getRound))
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
						m.getPredictionType().equals(LookUpEnumUtils.lookup(PredictionType.class, predictionType)) :
						predictionTypes.contains(m.getPredictionType()))
				.collect(Collectors.toList());
	}

	private List<FootballMatch> filterByMatchStatus(List<FootballMatch> filtered, String matchStatus) {

		if (StringUtils.isBlank(matchStatus))
			return filtered;

		return filtered.stream()
				.filter(m1 -> m1.getMatchStatus().equals(LookUpEnumUtils.lookup(MatchStatus.class, matchStatus)))
				.collect(Collectors.toList());
	}

	public void updateMatches(List<FootballMatch> matches) {

		for (FootballMatch match : matches) {

			try {
				if (!exist(match))
					throw new FootballMatchNotFoundExceptions(
							String.format("Cannot update football match %s. Doesnt exist in the data base", match.getSummary()));

				// Retrieve the match from the data base
				FootballMatch retrievedMatch = retrieve(match);

				// Don't perform update if the retrieved match is finished and have prediction
				if (retrievedMatch.getMatchStatus().equals(MatchStatus.FINISHED) &&
						!retrievedMatch.getPredictionType().equals(PredictionType.NOT_PREDICTED)) {
					log.warn("The match {} in the data base is considered already finished. No changes will apply",
							retrievedMatch.getSummary());
					continue;
				}

				FootballMatch updated = new FootballMatchBuilder(retrievedMatch)
						.setStatus(updatedStatus(retrievedMatch.getMatchStatus(), match.getMatchStatus()))
						.setMatchMetaData(updatedMetadata(retrievedMatch.getMatchMetaData(), match.getMatchMetaData()))
						.setPrediction(updatedPrediction(retrievedMatch.getPrediction(), match.getPrediction()))
						.setResult(updatedResult(retrievedMatch.getResult(), match.getResult()))
						.build();

				footballMatchRepository.save(updated);
				log.info("--MATCH {} updated.", updated.getSummary());

			} catch (Exception e) {
				log.error("Failed to update match {}", match.getSummary(), e);
			}
		}
	}

	private MatchStatus updatedStatus(MatchStatus m1, MatchStatus m2) {

		if (m2 == null)
			return m1;

		if (!m2.equals(MatchStatus.NOT_STARTED))
			return m2;

		return m1.equals(MatchStatus.NOT_STARTED) ? m2 : m1;
	}

	private MatchMetaData updatedMetadata(MatchMetaData m1, MatchMetaData m2) {
		return m1 == null ? m2 : m1;
	}

	private String updatedPrediction(String p1, String p2) {
		return StringUtils.isBlank(p1) ? p2 : p1;
	}

	private String updatedResult(String r1, String r2) {

		if (StringUtils.isBlank(r2))
			return r1;

		return StringUtils.isBlank(r1) || r1.equals(ResultMessages.UNKNOWN_RESULT) ? r2 : r1;
	}

	public int correctPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndMatchStatus(PredictionType.CORRECT, MatchStatus.FINISHED).size();
	}

	public int incorrectPredictedMatchesCount() {
		return footballMatchRepository.findByPredictionTypeAndMatchStatus(PredictionType.INCORRECT, MatchStatus.FINISHED).size();
	}

	public int matchesCount() {
		return (int) footballMatchRepository.count();
	}

	public void deleteAll() {
		footballMatchRepository.deleteAll();
		log.info("All matches are successfully deleted");
	}

}
