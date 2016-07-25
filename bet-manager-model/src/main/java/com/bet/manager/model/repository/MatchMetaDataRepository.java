package com.bet.manager.model.repository;

import com.bet.manager.model.dao.MatchMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchMetaDataRepository extends JpaRepository<MatchMetaData, Integer> {

	List<MatchMetaData> findByHomeTeam(String homeTeam);
}
