package com.bet.manager.commons.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ClasspathUtils {

	private ClasspathUtils() {
	}

	public static String getContentUTF8(String path) {

		String content;

		try (InputStream is = new BufferedInputStream(
				ClasspathUtils.class.getClassLoader().getResourceAsStream(path))) {
			content = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read the resource " + path);
		}

		return content;
	}

	public static String getContentISO(String path) {

		String content;

		try (InputStream is = new BufferedInputStream(
				ClasspathUtils.class.getClassLoader().getResourceAsStream(path))) {
			content = IOUtils.toString(is, "ISO8859_9");
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read the resource " + path);
		}

		return content;
	}
}
