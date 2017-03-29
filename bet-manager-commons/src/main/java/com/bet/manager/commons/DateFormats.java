package com.bet.manager.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormats {

	private DateFormats() {
	}

	public final static DateFormat MATCH_DATE_AND_TIME_FORMATTED = new SimpleDateFormat("MMMMM d yyyy HH:mm");
	public final static DateFormat CONCATENATED_DATE_AND_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final DateFormat FOOTBALL_DATA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	static {
		MATCH_DATE_AND_TIME_FORMATTED.setTimeZone(TimeZone.getTimeZone("UTC"));
		CONCATENATED_DATE_AND_TIME.setTimeZone(TimeZone.getTimeZone("UTC"));
		FOOTBALL_DATA_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));

		MATCH_DATE_AND_TIME_FORMATTED.setLenient(false);
		CONCATENATED_DATE_AND_TIME.setLenient(false);
	}

}
