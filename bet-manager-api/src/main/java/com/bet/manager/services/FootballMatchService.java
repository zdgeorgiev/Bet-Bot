package com.bet.manager.services;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.repository.FootballMatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class FootballMatchService {

	private static Logger log = LoggerFactory.getLogger(FootballMatchService.class);

	@Autowired
	private FootballMatchRepository footballMatchRepository;

	public int createMatches(List<FootballMatch> matches) {
		return 0;
	}

	public Collection<FootballMatch> retrieveAll() {
		return null;
	}

	public Page<FootballMatch> retrieveMatches(String team1, String team2, Optional<Integer> year, Optional<Integer> round,
			boolean correctPrediction, boolean finished, int limit, int offset) {
		return null;
	}

	public int matchesCount() {
		return 0;
	}

	public void deleteAll() {
	}

	public int updateMatch(List<FootballMatch> match) {

		return 0;
	}
}
