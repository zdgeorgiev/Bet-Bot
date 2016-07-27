package com.bet.manager.model.repository;

import com.bet.manager.model.dao.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

	Collection<FootballMatch> findByWinnerNotAndCorrectlyPredicted(String winnerType, boolean correctlyPredicted);

	FootballMatch findByHomeTeamAndAwayTeamAndStartDate(String homeTeam, String awayTeam, Date date);
}
