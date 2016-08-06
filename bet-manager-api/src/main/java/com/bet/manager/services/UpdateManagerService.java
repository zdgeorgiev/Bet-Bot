package com.bet.manager.services;

import com.bet.manager.core.IMatchParser;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.ai.IPredictor;
import com.bet.manager.core.data.DataManager;
import com.bet.manager.model.dao.FootballMatch;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateManagerService {

	private static final Logger log = LoggerFactory.getLogger(UpdateManagerService.class);

	private static final String FETCH_BASE_URL = "http://www.livescore.com/soccer/germany/bundesliga/fixtures/30-days/";

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

	@Scheduled(initialDelay = 5 * 1000, fixedDelay = 60 * 60 * 1000)
	public void fetch() throws MalformedURLException, InterruptedException {

		String content = WebCrawler.crawl(new URL(FETCH_BASE_URL));

		List<FootballMatch> newMatches = fetchMatches(content).stream().filter(m -> !exist(m)).collect(Collectors.toList());

		footballMatchService.createMatches(newMatches);
	}

	private List<FootballMatch> fetchMatches(String content) {
		return matchParser.parse(content);
	}

	@Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 60 * 1000)
	public void process() {

		footballMatchRepository.findAll().stream()
				.filter(m -> exist(m) && retrieve(m).getMatchMetaData() == null)
				.forEach(m -> {
					try {
						m = new FootballMatchBuilder(m).setMatchMetaData(
								dataManager.createFootballMatch(
										m.getHomeTeam(), m.getAwayTeam(), m.getYear(), m.getRound()).getMatchMetaData())
								.build();
					} catch (Exception e) {
						log.error("Error occur during creation metadata for match {}", m.getSummary(), e);
					}
				});
	}

	@Scheduled(initialDelay = 15 * 1000, fixedDelay = 60 * 60 * 1000)
	public void predict() {

		List<FootballMatch> matchesWithoutPrediction = footballMatchRepository.findAll().stream()
				.filter(m -> m.getPredictionType().equals(PredictionType.NOT_PREDICTED))
				.collect(Collectors.toList());

		log.info("Starting to make predictions for {} matches", matchesWithoutPrediction.size());
		matchesWithoutPrediction.stream()
				.forEach(m -> m = new FootballMatchBuilder(m).setPrediction(predictor.predict(m)).build());
		log.info("Prediction finished");

		footballMatchService.updateMatches(matchesWithoutPrediction);
	}

	private boolean exist(FootballMatch match) {
		return retrieve(match) != null;
	}

	private FootballMatch retrieve(FootballMatch match) {
		return footballMatchRepository
				.findByHomeTeamAndAwayTeamAndYearAndRound(match.getHomeTeam(), match.getAwayTeam(), match.getYear(),
						match.getRound());
	}
}
