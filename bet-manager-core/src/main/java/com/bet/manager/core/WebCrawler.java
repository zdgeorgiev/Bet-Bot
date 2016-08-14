package com.bet.manager.core;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class WebCrawler {

	private static final Logger log = LoggerFactory.getLogger(WebCrawler.class);

	private static final String USER_AGENT = "Mozilla/5.0";

	private WebCrawler() {
	}

	/**
	 * Crawl specific page with ISO8859_9 Encoding by default!!
	 *
	 * @param url url of the page
	 * @return the content of a page
	 */
	public static String crawl(URL url) throws InterruptedException {
		return crawl(url, Collections.emptyMap());
	}

	/**
	 * Crawling method which uses a external hashmap for memorization, not to crawl already crawled pages.
	 *
	 * @param url          url of the page
	 * @param crawledPages memorization map
	 * @return the content of a page
	 */
	public static String crawl(URL url, Map<URL, String> crawledPages) throws InterruptedException {
		return crawl(url, crawledPages, "ISO8859_9", 3, 5);
	}

	/**
	 * Crawling method which uses a external hashmap for memorization, not to crawl already crawled pages.
	 * Also this method is using Thread.sleep() between the requests.
	 *
	 * @param url             url of the page
	 * @param crawledPages    memorization map
	 * @param minSecondsSleep minimum sleep time between each request
	 * @param maxSecondsSleep maximum sleep time between each request
	 * @return the content of a page
	 */
	public static String crawl(URL url, Map<URL, String> crawledPages, String encoding, int minSecondsSleep, int maxSecondsSleep)
			throws InterruptedException {
		if (crawledPages.containsKey(url)) {
			log.debug("Returning cached copy of '{}'", url);
			return crawledPages.get(url);
		}

		// Put asleep the thread for [minSecondsSleep,maxSecondsSleep] seconds
		Thread.sleep(
				new Random().nextInt((maxSecondsSleep - minSecondsSleep) * 1000) + minSecondsSleep * 1000);

		String contentOfPage = getContent(url, encoding);

		try {
			crawledPages.put(url, contentOfPage);
		} catch (Exception e) {
			// This catch block is leaved empty not incidentally.
			// If the collection is Collections.emptyMap() items cannot be added and will throw exception
		}

		return contentOfPage;
	}

	private static String getContent(URL page, String encoding) {

		String content;

		try {
			HttpURLConnection con = (HttpURLConnection) page.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);

			log.debug("Sending 'GET' request to URL : {}", page);

			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream(), encoding))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				content = response.toString();
			}

			con.disconnect();

			if (StringUtils.isBlank(content)) {
				throw new IllegalStateException(
						"Content of the page '" + page.toString() + "' cannot be empty.");
			}

			log.debug("Successfully crawled url - '{}'", page.toString());
			return content;

		} catch (Exception e) {
			throw new IllegalStateException("Cannot get content of the page '" + page.toString() + "'.");
		}
	}
}
