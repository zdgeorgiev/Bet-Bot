package com.bet.manager.model.repository;

import com.bet.manager.model.entity.FootballMatch;

public interface FootballMatchRepositoryCustom {

	/**
	 * Return match from the db if exist otherwise null
	 *
	 * @param match match
	 * @return match from the db or null if not exist
	 */
	FootballMatch retrieve(FootballMatch match);

	/**
	 * Check if given match exist in the database
	 *
	 * @param match match
	 * @return true if the match exist otherwise false
	 */
	boolean exist(FootballMatch match);
}
