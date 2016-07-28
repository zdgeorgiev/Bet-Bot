package com.bet.manager.model.repository;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.PredictionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

	Collection<FootballMatch> findByPredictionTypeAndFinishedTrue(PredictionType predictionType);

	FootballMatch findByHomeTeamAndAwayTeamAndYearAndRound(String homeTeam, String awayTeam, int year, int round);
}
