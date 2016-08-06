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
import org.springframework.util.CollectionUtils;

import java.net.URL;
import java.util.ArrayList;
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

	@Scheduled(initialDelay = 30 * 60000, fixedDelay = 30 * 60000)
	public void fetch() {

		new Thread(() -> {
			String content = null;

			try {
				content = WebCrawler.crawl(new URL(FETCH_BASE_URL));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// Fetch all future matches
			List<FootballMatch> matches = matchParser.parse(content);

			// Create metadata for the fetched matches
			createMetaDataForMatches(matches);

			// Create predictions
			createPredictions(matches.stream()
					.filter(m -> m.getPredictionType().equals(PredictionType.NOT_PREDICTED))
					.collect(Collectors.toList()));

			updateDB(matches);
		}).run();
	}

	private void createMetaDataForMatches(List<FootballMatch> matches) {

		for (FootballMatch match : matches) {

			if (exist(match) && retrieve(match).getMatchMetaData() != null)
				continue;

			try {

				match = new FootballMatchBuilder(match)
						.setMatchMetaData(dataManager.createFootballMatch(
								match.getHomeTeam(), match.getAwayTeam(), 2011, 3).getMatchMetaData())
						.build();

			} catch (Exception e) {
				log.error("Exception occur during creation of metadata for match {}", match.getSummary(), e);
			}
		}
	}

	public void predictAll() {

		List<FootballMatch> matchesWithoutPrediction = footballMatchRepository.findAll().stream()
				.filter(m -> m.getPredictionType().equals(PredictionType.NOT_PREDICTED))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(matchesWithoutPrediction)) {
			log.info("Not found matches without prediction.. breaking..");
			return;
		}

		createPredictions(matchesWithoutPrediction);
		footballMatchService.updateMatches(matchesWithoutPrediction);
	}

	private void createPredictions(List<FootballMatch> matchesWithoutPrediction) {
		log.info("Starting to make predictions for {} matches", matchesWithoutPrediction.size());
		matchesWithoutPrediction.stream().forEach(m -> m = new FootballMatchBuilder(m).setPrediction(predictor.predict(m)).build());
		log.info("Prediction finished");
	}

	private void updateDB(List<FootballMatch> matches) {

		List<FootballMatch> matchesToCreate = new ArrayList<>(matches.size());
		List<FootballMatch> matchesToUpdate = new ArrayList<>(matches.size());

		matches.stream().filter(m -> !exist(m)).collect(Collectors.toCollection(() -> matchesToCreate));
		matches.stream().filter(this::exist).collect(Collectors.toCollection(() -> matchesToUpdate));

		if (matchesToCreate.size() == 0 && matchesToUpdate.size() == 0) {
			log.info("No data will be saved in the data base");
			return;
		}

		log.info("Sending to data base..");
		footballMatchService.createMatches(matchesToCreate);
		footballMatchService.updateMatches(matchesToUpdate);
		log.info("Done..");
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
