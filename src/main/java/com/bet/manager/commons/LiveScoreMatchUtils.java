package com.bet.manager.commons;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.jsoup.nodes.Element;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LiveScoreMatchUtils {

    private static final Logger log = LoggerFactory.getLogger(LiveScoreMatchUtils.class);

    private static final Calendar cal = Calendar.getInstance();

    private static final String TEAM_DIV_SELECTOR = "div.ply.name";
    private static final String START_TIME_DIV_SELECTOR = "div.min";

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM d yyyy HH:mm");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("EET"));
    }

    public LiveScoreMatchUtils() {

    }

    /**
     * Return match created from div html for starting date only if
     * there is no errors occurred during the creation, else it will
     * return <code>NULL</code>
     *
     * @param div  - html div containing the specific <class>Match</class>
     * @param date the day and month when the match will start as <class>String</class>
     *             in specific date format [MMMMM d]
     * @return <class>Match</class> object
     */
    public Match create(Element div, String date) {

        String homeTeam = getHomeTeam(div);
        String awayTeam = getAwayTeam(div);
        String startTime = getStartTime(div);

        if (!checkValidTeam(homeTeam, awayTeam) ||
                !checkValidStartTime(startTime) ||
                !checkValidDate(date)) {

            log.info("Skipping match with div\n{}", div.toString());
            return null;
        }

        Date startDate = addTimeToDate(date, startTime);
        return createMatch(homeTeam, awayTeam, startDate);
    }

    private String getHomeTeam(Element div) {
        return getTeam(div, true);
    }

    private String getAwayTeam(Element div) {
        return getTeam(div, false);
    }

    private String getTeam(Element div, boolean isHomeTeam) {
        int index = isHomeTeam == true ? 0 : 1;
        return div.select(TEAM_DIV_SELECTOR).get(index).text().trim();
    }

    private String getStartTime(Element div) {
        return div.select(START_TIME_DIV_SELECTOR).text().trim();
    }

    private boolean checkValidTeam(String homeTeam, String awayTeam) {
        if (StringUtil.isBlank(homeTeam)) {
            log.error("Home team name cannot be null or empty");
            return false;
        }

        if (StringUtil.isBlank(awayTeam)) {
            log.error("Away team name cannot be null or empty");
            return false;
        }

        if (homeTeam.equals(awayTeam)) {
            log.error("Home team '" + homeTeam + "' cannot be the same as away team");
            return false;
        }

        return true;
    }

    private boolean checkValidStartTime(String startTime) {

        if (StringUtil.isBlank(startTime)) {
            log.error("Start time cannot be null or empty");
            return false;
        }

        return true;
    }

    private boolean checkValidDate(String date) {

        try {
            new SimpleDateFormat("MMMMM d").parse(date);
        } catch (ParseException e) {
            log.error("Start date is not in the valid format {}", date);
            return false;
        }

        return true;
    }

    private Date addTimeToDate(String date, String time) {

        Date finalDate;
        String finalDateString = "";

        try {
            finalDateString = String.format("%s %s %s", date, cal.get(Calendar.YEAR), time);
            finalDate = DATE_FORMAT.parse(finalDateString);
        } catch (ParseException e) {
            log.error("Cannot create date - '" + finalDateString + "'");
            throw new IllegalStateException();
        }

        return finalDate;
    }

    private Match createMatch(String homeTeam, String awayTeam, Date startDate) {
        Match m = new FootballMatch(homeTeam, awayTeam, startDate);
        log.info("Successfully parsed - {}", m);
        return m;
    }
}
