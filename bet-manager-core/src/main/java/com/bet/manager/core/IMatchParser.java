package com.bet.manager.core;

import com.bet.manager.model.dao.Match;

import java.util.List;

public interface IMatchParser {

	/**
	 * Parse all matches from given content and store them in <class>List</class>
	 *
	 * @param content - html content containing the matches
	 * @return the collection from all successfully parsed matches
	 */
	List<Match> parse(String content);
}
