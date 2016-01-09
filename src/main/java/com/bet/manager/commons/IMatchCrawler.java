package com.bet.manager.commons;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * MatchCrawler is regular web crawler which crawl from given url and returns the result
 */
public interface IMatchCrawler {

    /**
     * Parse all matches from given content and store them in <class>List</class>
     *
     * @param page - url page containing the matches
     * @param handler - handler is specific match parser
     * @return the collection from all parsed matches
     */
    List<Match> getMatches(URL page, IParserHandler handler);

    /**
     * Schedule the next invocation to specific date.
     * The invocation is handled from the default
     * <class>ExecutorDispatcher</class>
     *
     * @param date - date when the getMatches method from IMatchCrawler
     *             should be executed
     */
    void scheduleForNextExecution(Date date);
}
