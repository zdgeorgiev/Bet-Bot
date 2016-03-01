package com.bet.manager.core;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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

		File inputFile = new File("bet-manager-core/src/test/resources/post_standing_1.xml");
		String content = FileUtils.readFileToString(inputFile);

		DataCrawlerUtils.createDataForRound(content, 2011, 1);
	}
}
