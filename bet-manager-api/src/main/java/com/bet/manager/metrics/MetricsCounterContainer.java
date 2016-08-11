package com.bet.manager.metrics;

import com.bet.manager.services.FootballMatchService;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Service
public class MetricsCounterContainer {

	@Inject
	private MetricRegistry metricRegistry;

	private Counter metadataSuccess;
	private Counter metadataFailures;

	private Counter predictionsSuccess;
	private Counter predictionsFailures;

	@PostConstruct
	public void init() {

		metadataSuccess = metricRegistry.counter(MetricRegistry.name(FootballMatchService.class, "meta-data-successes"));
		metadataFailures = metricRegistry.counter(MetricRegistry.name(FootballMatchService.class, "meta-data-failures"));

		predictionsSuccess = metricRegistry.counter(MetricRegistry.name(FootballMatchService.class, "predictions-successes"));
		predictionsFailures = metricRegistry.counter(MetricRegistry.name(FootballMatchService.class, "predictions-failures"));
	}

	public Counter getMetadataSuccess() {
		return metadataSuccess;
	}

	public Counter getMetadataFailures() {
		return metadataFailures;
	}

	public Counter getPredictionsSuccess() {
		return predictionsSuccess;
	}

	public Counter getPredictionsFailures() {
		return predictionsFailures;
	}

	public void incMetadataSuccesses() {
		metadataSuccess.inc();
	}

	public void incMetadataFailures() {
		metadataFailures.inc();
	}

	public void incPredictionsSuccesses() {
		predictionsSuccess.inc();
	}

	public void incPredictionsFailures() {
		predictionsFailures.inc();
	}
}
