package com.bet.manager.model.repository;

import com.bet.manager.model.dao.MatchMetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchMetaDataRepository extends JpaRepository<MatchMetaData, Integer> {

	MatchMetaData findByHomeTeamAndAwayTeamAndYearAndRound(String homeTeam, String awayTeam, int year, int round);

	//	Page<MatchMetaData> findByHomeTeam(String homeTeam, String awayTeam, Optional<Integer> year, Optional<Integer> round,
	//			Pageable pageable);

	Page<MatchMetaData> findByHomeTeam(String homeTeam, Pageable pageable);
}
