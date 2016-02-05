package com.bet.manager.core;

import java.net.URL;

public interface IWebCrawler {

	/**
	 * Crawl the given url and return its content
	 *
	 * @param url
	 * @return
	 */
	String crawl(URL url);
}
