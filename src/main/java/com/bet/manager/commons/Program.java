package com.bet.manager.commons;

import java.net.MalformedURLException;
import java.net.URL;

public class Program {

    public static void main(String[] args) throws MalformedURLException {

        URL liveScoreURL = new URL("http://www.livescore.com/soccer/england/premier-league/fixtures/7-days/");

        LiveScoreMatchCrawler crawler = new LiveScoreMatchCrawler();
        crawler.getMatches(liveScoreURL, new LiveScoreFootballParserHandler());
    }
}
