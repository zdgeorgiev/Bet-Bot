package com.bet.manager.commons.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ClasspathUtils {

	private ClasspathUtils() {
	}

	public static String getContentUTF8(String path) {
		return getContentWithEncoding(path, "UTF-8");
	}

	public static String getContentISO(String path) {
		return getContentWithEncoding(path, "ISO8859_9");
	}

	private static String getContentWithEncoding(String path, String encoding) {
		String content;

		try (InputStream is = new BufferedInputStream(
				ClasspathUtils.class.getClassLoader().getResourceAsStream(path))) {
			content = IOUtils.toString(is, encoding);
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read the resource " + path);
		}

		return content;
	}
}
