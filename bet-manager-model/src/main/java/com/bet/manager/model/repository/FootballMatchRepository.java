package com.bet.manager.model.repository;

import com.bet.manager.model.dao.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballMatchRepository extends JpaRepository<FootballMatch, Integer> {

}
