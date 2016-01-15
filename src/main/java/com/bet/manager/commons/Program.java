package com.bet.manager.commons;

import java.net.MalformedURLException;
import java.net.URL;

public class Program {

    public static void main(String[] args) throws MalformedURLException {

        URL liveScoreURL = new URL("http://www.livescore.com/soccer/england/premier-league/fixtures/7-days/");

        IMatchCrawler crawler = new LiveScoreMatchCrawler();

        String liveScoreContent = crawler.crawl(liveScoreURL);

        crawler.getMatches(liveScoreContent).stream().forEach((x) -> System.out.println(x));
    }
}
