package com.bet.manager.core;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebCrawler {

	private static final Logger log = LoggerFactory.getLogger(WebCrawler.class);
	private static final String USER_AGENT = "Mozilla/5.0";

	public String crawl(URL page) {
		checkValidPage(page);
		return getContent(page);
	}

	private void checkValidPage(URL page) {
		if (page == null) {
			throw new IllegalArgumentException("URL page cannot be null");
		}
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
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				content = response.toString();
			}

			if (StringUtils.isBlank(content)) {
				throw new IllegalStateException("Content of the page '" + page.toString() + "' cannot be empty.");
			}

			log.info("Successfully crawled url - '{}'", page.toString());
			return content;

		} catch (Exception e) {
			throw new IllegalStateException("Cannot get content of the page '" + page.toString() + "'.");
		}
	}
}
