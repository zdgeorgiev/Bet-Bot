package com.bet.manager.commons;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

public interface IMatchParser {

    /**
     * Parse all matches from given content and store them in <class>List</class>
     *
     * @param content - html content containing the matches
     * @return the collection from all successfully parsed matches
     */
    List<Match> parseAll(String content);
}
