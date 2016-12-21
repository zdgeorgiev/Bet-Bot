package com.bet.manager.core.data;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchMetaData;

public interface DataManager<T extends FootballMatch> {

	/**
	 * Method which is used to create the {@link MatchMetaData} for given match.
	 *
	 * @param match match for which we want to generate meta data
	 */
	void createData(T match) throws Exception;
}