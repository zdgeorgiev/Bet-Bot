package com.bet.manager.core;

import com.bet.manager.models.dao.Match;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

public class Program {

	public static void main(String[] args) throws IOException, ParseException {

		BasicConfigurator.configure();

		WebCrawler crawler = new WebCrawler();
		IMatchParser parser = new LiveScoreMatchParser();

		List<Match> matches =
				parser.parse(
						crawler.crawl(
								new URL("http://www.livescore.com/soccer/germany/2-bundesliga/results/30-days/")));

		matches.stream().forEach((x) -> System.out.println(x));
	}
}
