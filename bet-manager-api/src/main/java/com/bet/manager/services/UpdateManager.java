package com.bet.manager.services;

import com.bet.manager.core.IMatchParser;
import com.bet.manager.core.WebCrawler;
import com.bet.manager.core.ai.IPredictor;
import com.bet.manager.core.data.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateManager {

	private static final Logger log = LoggerFactory.getLogger(UpdateManager.class);

	@Autowired
	private FootballMatchService footballMatchService;

	@Autowired
	private IPredictor predictor;

	@Autowired
	private DataManager dataManager;

	@Autowired
	private IMatchParser matchParser;

	@Autowired
	private WebCrawler crawler;

	@Scheduled(initialDelay = 3 * 1000, fixedDelay = 30 * 1000)
	public void run() {
		log.warn("OMG ITS HAPPENING");
	}
}
