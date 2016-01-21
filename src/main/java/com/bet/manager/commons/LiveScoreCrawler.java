package com.bet.manager.commons;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class LiveScoreCrawler implements IWebCrawler {

	private static final Logger log = LoggerFactory.getLogger(LiveScoreCrawler.class);

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

		try (InputStream is = new BufferedInputStream(page.openStream())) {
			return IOUtils.toString(is, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read url - '" + page.toString() + "'");
		}
	}
}
