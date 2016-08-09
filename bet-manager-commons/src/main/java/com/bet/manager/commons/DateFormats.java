package com.bet.manager.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormats {

	private DateFormats() {
	}

	public final static DateFormat LIVE_SCORE_MATCH_DATE_FORMATTED = new SimpleDateFormat("MMMMM d yyyy HH:mm");
	public final static DateFormat LIVE_SCORE_MATCH_START_DATE_AND_TIME = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final DateFormat FOOTBALL_DATA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

	static {
		LIVE_SCORE_MATCH_DATE_FORMATTED.setTimeZone(TimeZone.getTimeZone("EET"));
		LIVE_SCORE_MATCH_START_DATE_AND_TIME.setTimeZone(TimeZone.getTimeZone("EET"));
		//FOOTBALL_DATA_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("EET"));

		LIVE_SCORE_MATCH_DATE_FORMATTED.setLenient(false);
		LIVE_SCORE_MATCH_START_DATE_AND_TIME.setLenient(false);
		//FOOTBALL_DATA_DATE_FORMAT.setLenient(false);
	}

}
