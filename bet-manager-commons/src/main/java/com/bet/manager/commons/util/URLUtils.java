package com.bet.manager.commons.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLUtils {

	private URLUtils() {
	}

	public static URL createSafeURL(String url) throws MalformedURLException {
		return new URL(url.replace(" ", "%20"));
	}
}
