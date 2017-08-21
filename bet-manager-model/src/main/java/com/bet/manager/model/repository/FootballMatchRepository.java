package com.bet.manager.model.repository;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.dao.PredictionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Long> {

	List<FootballMatch> findByPredictionTypeAndMatchStatus(PredictionType predictionType, MatchStatus matchStatus);

	List<FootballMatch> findByPredictionType(PredictionType predictionType);

	List<FootballMatch> findByMatchStatus(MatchStatus matchStatus);

	FootballMatch findByHomeTeamAndAwayTeamAndYearAndRound(String homeTeam, String awayTeam, int year, int round);
}
