package com.bet.manager.core;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

public class Program {

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

		//		BasicConfigurator.configure();
		//
		//		WebCrawler crawler = new WebCrawler();
		//		IMatchParser parser = new LiveScoreMatchParser();
		//		IPredictor predictor = new SimplePredictor();
		//
		//		List<Match> matches =
		//				parser.parse(
		//						crawler.crawl(
		//								new URL("http://www.livescore.com/soccer/germany/2-bundesliga/results/30-days/")));
		//
		//		matches.stream().forEach((x) -> System.out.println(x));
		//
		//		System.out.println("WINNERS");
		//
		//		matches.stream().forEach((x) -> System.out.println(predictor.predict(x)));

		String s = new WebCrawler().crawl(new URL(
				"http://www.bundesliga.com/data/feed/51/2011/team_stats_round/team_stats_round_1.xml?cb=544329"));


		DocumentUtils.parse(s);
	}
}
