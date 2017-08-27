package com.bet.manager.model.repository;

import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Long>, FootballMatchRepositoryCustom {

	List<FootballMatch> findByPredictionTypeAndMatchStatus(PredictionType predictionType, MatchStatus matchStatus);

	List<FootballMatch> findByPredictionType(PredictionType predictionType);
}
