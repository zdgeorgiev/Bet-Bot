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
import com.bet.manager.model.repository.FootballMatchRepository;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UpdateManagerService {

	private static final Logger log = LoggerFactory.getLogger(UpdateManagerService.class);

	private static final String FETCH_BASE_URL =
			"http://api.football-data.org/v1/fixtures?league=BL1&timeFrameStart=%s&timeFrameEnd=%s";

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Calendar CALENDAR = Calendar.getInstance();

	@Autowired
	private FootballMatchService footballMatchService;

	@Autowired
	private FootballMatchRepository footballMatchRepository;

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

		String startDate = getDateAfterDays(-3);
		String endDate = getDateAfterDays(14);

		int acceptedRound = getAcceptedRound();

		String matchesURL = String.format(FETCH_BASE_URL, startDate, endDate);

		log.info("Starting to fetch matches for round [{}] from source [{}]", acceptedRound, matchesURL);
		String content = WebCrawler.crawl(new URL(matchesURL));

		updateDataBase(content, acceptedRound);

		log.info("Finished fetching");
	}

	private void updateDataBase(String content, int acceptedRound) {

		Map<MatchStatus, List<FootballMatch>> fixtures = matchParser.parse(content);

		footballMatchService.createMatches(fixtures.get(MatchStatus.NOT_STARTED).stream()
				.filter(m -> m.getRound() == acceptedRound && !footballMatchService.exist(m))
				.collect(Collectors.toList()));

		footballMatchService.updateMatches(fixtures.get(MatchStatus.STARTED).stream()
				.filter(m -> m.getRound() == acceptedRound)
				.collect(Collectors.toList()));

		footballMatchService.updateMatches(fixtures.get(MatchStatus.FINISHED).stream()
				.filter(m -> m.getRound() == acceptedRound)
				.collect(Collectors.toList()));
	}

	private String getDateAfterDays(int days) {

		CALENDAR.setTime(new Date());
		CALENDAR.add(Calendar.DATE, days);
		return DATE_FORMAT.format(CALENDAR.getTime());
	}

	private int getAcceptedRound() {
		Optional<FootballMatch> match = footballMatchRepository.findAll().stream()
				.filter(m ->
						m.getMatchStatus().equals(MatchStatus.FINISHED) && m.getMatchMetaData() != null)
				.sorted((FootballMatch m1, FootballMatch m2) ->
						Integer.compare(m1.getRound(), m2.getRound()))
				.findFirst();

		if (match.isPresent())
			return match.get().getRound() + 1;

		// We accept matches for at least 2nd round
		return 2;
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
