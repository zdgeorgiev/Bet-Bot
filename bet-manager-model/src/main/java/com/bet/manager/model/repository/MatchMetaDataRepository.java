package com.bet.manager.model.repository;

import com.bet.manager.model.entity.MatchMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchMetaDataRepository extends JpaRepository<MatchMetaData, Long> {
}
