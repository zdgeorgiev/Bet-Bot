package com.bet.manager.commons;

import java.util.Date;

public interface IParserHandler {

    /**
     * Parse match from given string
     *
     * @param matchAsHtmlString - content from html containing the specific <class>Match</class>
     *                          as html string
     * @param startingDate      the day and year when the match will start as <class>Date</class>
     * @return <class>Match</class> object
     */
    Match parse(String matchAsHtmlString, Date startingDate);
}
