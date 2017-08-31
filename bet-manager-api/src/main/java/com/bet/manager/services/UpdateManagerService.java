package com.bet.manager.services;

import com.bet.manager.commons.util.PerformanceUtils;
import com.bet.manager.core.IMatchParser;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.ai.IPredictor;
import com.bet.manager.core.data.DataManager;
import com.bet.manager.metrics.MetricsCounterContainer;
import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import com.bet.manager.model.repository.FootballMatchRepository;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UpdateManagerService {

	private static final Logger LOG = LoggerFactory.getLogger(UpdateManagerService.class);

	private static final String FETCH_BASE_URL =
			"http://api.football-data.org/v1/fixtures?league=BL1&timeFrameStart=%s&timeFrameEnd=%s";

	@Autowired
	private FootballMatchService footballMatchService;

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	@Autowired
	private IPredictor predictor;

	@Autowired
	private DataManager<FootballMatch> dataManager;

	@Autowired
	private IMatchParser matchParser;

	@Autowired
	private MetricsCounterContainer metricsCounterContainer;

	@Scheduled(initialDelay = 5 * 1000, fixedDelay = 60 * 60 * 1000)
	public void fetch() throws MalformedURLException, InterruptedException {

		LocalDate now = LocalDate.now();

		String matchesURL = String.format(FETCH_BASE_URL,
				now.minusDays(3),
				now.plusDays(14));

		LOG.info("Starting to fetch matches from [{}]", matchesURL);
		String matchesContentFeed = WebCrawler.crawl_UTF8(new URL(matchesURL));

		updateDataBase(matchesContentFeed);
		LOG.info("Finished fetching");
	}

	private void updateDataBase(String matchesFeed) {

		Map<MatchStatus, List<FootballMatch>> fixtures = matchParser.parse(matchesFeed);

		List<FootballMatch> allMatches = new ArrayList<>();
		allMatches.addAll(fixtures.get(MatchStatus.NOT_STARTED));
		allMatches.addAll(fixtures.get(MatchStatus.STARTED));
		allMatches.addAll(fixtures.get(MatchStatus.FINISHED));

		footballMatchService.createMatches(
				allMatches.stream()
						.filter(m -> !footballMatchRepository.exist(m))
						.collect(Collectors.toList()));

		footballMatchService.updateMatches(allMatches);
	}

	@Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 60 * 1000)
	public void process() {

		List<FootballMatch> matchesWithoutMetadata = new ArrayList<>();

		long start = System.currentTimeMillis();
		LOG.info("Starting to create metadata for the matches");

		footballMatchRepository.findAll().stream()
				.filter(m -> m.getMatchMetaData() == null)
				.forEach(m -> {
					try {

						FootballMatch updatedMatch = dataManager.createData(m);
						matchesWithoutMetadata.add(updatedMatch);

						metricsCounterContainer.incMetadataSuccesses();
					} catch (Exception e) {
						metricsCounterContainer.incMetadataFailures();
						LOG.error("Error occur during creating metadata for match {}", m.getSummary(), e);
					}
				});

		long end = System.currentTimeMillis();
		LOG.info("Metadata creation finished in {}", PerformanceUtils.convertToHumanReadable(end - start));

		footballMatchService.updateMatches(matchesWithoutMetadata);
	}

	@Scheduled(initialDelay = 15 * 1000, fixedDelay = 60 * 60 * 1000)
	public void predict() {

		List<FootballMatch> matchesWithoutPrediction =
				footballMatchRepository.findByPredictionType(PredictionType.NOT_PREDICTED).stream()
						.filter(m -> m.getMatchMetaData() != null && StringUtils.isBlank(m.getPrediction()))
						.collect(Collectors.toList());

		if (matchesWithoutPrediction.size() == 0) {
			LOG.info("All matches in db are predicted or that who arent dont have metadata.");
			return;
		}

		long start = System.currentTimeMillis();
		LOG.info("Starting to make predictions for {} matches", matchesWithoutPrediction.size());

		List<FootballMatch> predicted = new ArrayList<>();

		matchesWithoutPrediction
				.forEach(m -> {
					try {
						m = new FootballMatchBuilder(m).setPrediction(predictor.predict(m)).build();
						predicted.add(m);
						metricsCounterContainer.incPredictionsSuccesses();
					} catch (Exception e) {
						metricsCounterContainer.incPredictionsFailures();
						LOG.error("Error occur during creation prediction for match {}", m.getSummary(), e);
					}
				});

		long end = System.currentTimeMillis();
		LOG.info("Prediction finished in {}", PerformanceUtils.convertToHumanReadable(end - start));

		footballMatchService.updateMatches(predicted);
	}
}
