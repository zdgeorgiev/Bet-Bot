package com.bet.manager.commons;

public interface IParserHandler {

    /**
     * Parse match from given string
     *
     * @param matchAsHtmlString - content from html containing the specific <class>Match</class>
     *                          as html string
     * @param startingDate      the day and month when the match will start as <class>String</class>
     * @return <class>Match</class> object
     */
    Match parse(String matchAsHtmlString, String startingDate);
}
