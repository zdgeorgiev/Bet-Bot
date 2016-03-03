package com.bet.manager.core;

import com.bet.manager.core.util.DataCrawlerUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

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


		DataCrawlerUtils.getDataForAllMatches(2011);
	}
}
