package com.bet.manager.services;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.IMatchParser;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.ai.IPredictor;
import com.bet.manager.core.data.DataManager;
import com.bet.manager.metrics.MetricsCounterContainer;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.dao.PredictionType;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UpdateManagerService {

	private static final Logger log = LoggerFactory.getLogger(UpdateManagerService.class);

	//	private static final String FETCH_BASE_URL = "http://api.football-data.org/v1/fixtures?league=BL1";
	private static final String FETCH_BASE_URL =
			"http://api.football-data.org/v1/fixtures?league=BL1&timeFrameStart=2016-08-25&timeFrameEnd=2016-09-10";

	@Autowired
	private FootballMatchService footballMatchService;

	@Autowired
	private IPredictor predictor;

	@Autowired
	private DataManager dataManager;

	@Autowired
	private IMatchParser matchParser;

	@Autowired
	private MetricsCounterContainer metricsCounterContainer;

	@Scheduled(initialDelay = 5 * 1000, fixedDelay = 60 * 60 * 1000)
	public void fetch() throws MalformedURLException, InterruptedException {

		log.info("Starting to fetch matches from source [{}]", FETCH_BASE_URL);
		String content = WebCrawler.crawl(new URL(FETCH_BASE_URL));

		Map<MatchStatus, List<FootballMatch>> fixtures = matchParser.parse(content);

		footballMatchService.createMatches(fixtures.get(MatchStatus.NOT_STARTED).stream()
				.filter(m -> !footballMatchService.exist(m))
				.collect(Collectors.toList()));
		footballMatchService.updateMatches(fixtures.get(MatchStatus.FINISHED));
	}

	@Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 60 * 1000)
	public void process() {

		List<FootballMatch> predictedMatches = new ArrayList<>();

		long start = System.currentTimeMillis();
		log.info("Starting to create meta data for the matches");

		footballMatchService.findAll().stream()
				.filter(m -> m.getMatchMetaData() == null)
				.forEach(m -> {
					try {
						predictedMatches.add(
								new FootballMatchBuilder(m).setMatchMetaData(
										dataManager.createFootballMatch(m.getHomeTeam(), m.getAwayTeam(), m.getYear(),
												m.getRound()).getMatchMetaData())
										.build());
						metricsCounterContainer.incMetadataSuccesses();
					} catch (Exception e) {
						metricsCounterContainer.incMetadataFailures();
						log.error("Error occur during creating metadata for match {}", m.getSummary(), e);
					}
				});

		long end = System.currentTimeMillis();
		log.info("Meta data creation finished in {}", PerformanceUtils.convertToHumanReadable(end - start));

		if (predictedMatches.size() == 0) {
			log.info("Not found matches without metadata");
			return;
		}

		footballMatchService.updateMatches(predictedMatches);
	}

	@Scheduled(initialDelay = 15 * 1000, fixedDelay = 60 * 60 * 1000)
	public void predict() {

		List<FootballMatch> matchesWithoutPrediction = footballMatchService.findAll().stream()
				.filter(m -> m.getPredictionType().equals(PredictionType.NOT_PREDICTED) && m.getMatchMetaData() != null)
				.collect(Collectors.toList());

		log.info("Starting to make predictions for {} matches", matchesWithoutPrediction.size());

		if (matchesWithoutPrediction.size() == 0) {
			log.info("Matches with meta data and without prediction are not found");
			return;
		}

		matchesWithoutPrediction.stream()
				.forEach(m -> {
					try {
						m = new FootballMatchBuilder(m).setPrediction(predictor.predict(m)).build();
						metricsCounterContainer.incPredictionsSuccesses();
					} catch (Exception e) {
						metricsCounterContainer.incPredictionsFailures();
						log.error("Error occur during creation prediction for match {}", m.getSummary(), e);
					}
				});

		log.info("Prediction finished");

		footballMatchService.updateMatches(matchesWithoutPrediction);
	}
}
