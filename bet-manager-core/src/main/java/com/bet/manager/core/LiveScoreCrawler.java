package com.bet.manager.core;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LiveScoreCrawler implements IWebCrawler {

	private static final Logger log = LoggerFactory.getLogger(LiveScoreCrawler.class);
	public static final String USER_AGENT = "Mozilla/5.0";

	private final String HTML_BODY_OPEN_TAG = "<body";
	private final String HTML_BODY_CLOSE_TAG = "</body";

	@Override
	public String crawl(URL page) {
		checkValidPage(page);
		return getContentBody(page);
	}

	private void checkValidPage(URL page) {
		if (page == null) {
			throw new IllegalArgumentException("URL page cannot be null - '" + page.toString() + "'");
		}
	}

	private String getContentBody(URL page) {

		String content = getContent(page);

		int bodyOpenTagIndex = content.indexOf(HTML_BODY_OPEN_TAG);
		int bodyCloseTagIndex = content.indexOf(HTML_BODY_CLOSE_TAG);

		String pageBody = content.substring(bodyOpenTagIndex, bodyCloseTagIndex + HTML_BODY_CLOSE_TAG.length());

		log.info("Successfully crawled url - '{}'", page.toString());
		return pageBody;
	}

	private String getContent(URL page) {

		String content;

		try {
			HttpURLConnection con = (HttpURLConnection) page.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);

			log.debug("Sending 'GET' request to URL : {}", page);

			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()))) {
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				content = response.toString();
			}

			if (StringUtils.isBlank(content)) {
				throw new IllegalStateException("Content of the page '" + page.toString() + "' cannot be empty.");
			}

			return content;

		} catch (Exception e) {
			throw new IllegalStateException("Cannot get content of the page '" + page.toString() + "'.");
		}
	}
}
