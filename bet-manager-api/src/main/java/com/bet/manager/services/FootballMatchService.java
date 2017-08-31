package com.bet.manager.services;

import com.bet.manager.exceptions.FootballMatchAlreadyExistException;
import com.bet.manager.exceptions.FootballMatchNotFoundExceptions;
import com.bet.manager.metrics.MetricsCounterContainer;
import com.bet.manager.metrics.SuccessRatioGauge;
import com.bet.manager.metrics.SuccessRatioHealthCheck;
import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
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
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FootballMatchService {

	private static final Logger LOG = LoggerFactory.getLogger(FootballMatchService.class);

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	@Autowired
	private MetricsCounterContainer metricsCounterHolder;

	@Autowired
	private MetricRegistry metricRegistry;

	@Autowired
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
				FootballMatch validatedMatch = new FootballMatchBuilder(match).build();

				if (footballMatchRepository.exist(validatedMatch))
					throw new FootballMatchAlreadyExistException(
							String.format("Football Match '%s' already exist", match.getSummary()));

				footballMatchRepository.save(validatedMatch);
				metricsCounterHolder.incMatchesSuccesses();
				LOG.info("Successfully created MATCH {}", match.getSummary());

			} catch (Exception e) {
				metricsCounterHolder.incMatchesFailures();
				LOG.warn("Failed to save football match in the database", e);
			}
		}
	}

	public void updateMatches(List<FootballMatch> matches) {

		int updatedMatches = 0;

		for (FootballMatch match : matches) {

			try {
				if (!footballMatchRepository.exist(match))
					throw new FootballMatchNotFoundExceptions(
							String.format("Cannot update football match %s. Doesnt exist in the data base", match.getSummary()));

				// Retrieve the match from the data base
				FootballMatch retrievedMatch = footballMatchRepository.retrieve(match);

				if (isMatchFinishedAndPredicted(retrievedMatch)) {
					LOG.debug("The match {} in the db is considered finished. No changes will apply", retrievedMatch.getSummary());
					continue;
				}

				FootballMatch updated = new FootballMatchBuilder(retrievedMatch)
						.updateStartDate(match.getStartDate())
						.updatedStatus(match.getMatchStatus())
						.updatedMetadata(match.getMatchMetaData())
						.updatedPrediction(match.getPrediction())
						.updatedResult(match.getResult())
						.build();

				if (!updated.equals(retrievedMatch)) {
					footballMatchRepository.save(updated);
					updatedMatches++;
					LOG.debug("--MATCH {} updated.", updated.getSummary());
				}
			} catch (Exception e) {
				LOG.error("Failed to update match {}", match.getSummary(), e);
			}
		}

		LOG.info("Successfully updated {} matches", updatedMatches);
	}

	private boolean isMatchFinishedAndPredicted(FootballMatch match) {
		return match.getMatchStatus().equals(MatchStatus.FINISHED) &&
				!StringUtils.isBlank(match.getPrediction());
	}

	public long matchesCount() {
		return footballMatchRepository.count();
	}

	public void deleteMatch(FootballMatch match) {
		FootballMatch retrievedMatch = footballMatchRepository.retrieve(match);

		if (retrievedMatch == null)
			throw new FootballMatchNotFoundExceptions(
					String.format("Cannot delete football match %s. Doesnt exist in the data base", match.getSummary()));

		footballMatchRepository.delete(retrievedMatch);
		LOG.info("Successfully deleted match [{}]", match.getSummary());
	}

	public void deleteAll() {
		footballMatchRepository.deleteAll();
		LOG.info("All matches are deleted successfully");
	}
}
