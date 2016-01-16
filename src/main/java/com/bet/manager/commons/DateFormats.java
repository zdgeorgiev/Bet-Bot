package com.bet.manager.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateFormats {

    public final static DateFormat LiVE_SCORE_MATCH_DATE_FORMAT = new SimpleDateFormat("MMMMM d yyyy HH:mm");
    public final static DateFormat LIVE_SCORE_TIME_FORMAT = new SimpleDateFormat("HH:mm");
    public final static DateFormat LIVE_SCORE_MONTH_DAY_FORMAT = new SimpleDateFormat("MMMMM d");

    static {
        LiVE_SCORE_MATCH_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("EET"));
        LIVE_SCORE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("EET"));
    }

}
