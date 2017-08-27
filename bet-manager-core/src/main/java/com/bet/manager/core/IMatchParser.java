package com.bet.manager.core;

import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;

import java.util.List;
import java.util.Map;

public interface IMatchParser {

	/**
	 * Parse all matches from given content and store them in <class>List</class>
	 *
	 * @param content - html content containing the matches
	 * @return the collection from all successfully parsed matches in categories of {@link MatchStatus}
	 */
	Map<MatchStatus, List<FootballMatch>> parse(String content);
}
