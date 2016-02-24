package com.bet.manager.core;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.models.dao.Match;
import com.bet.manager.models.util.FootballMatchBuilder;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveScoreMatchParser implements IMatchParser {

	private static final Logger log = LoggerFactory.getLogger(LiveScoreMatchParser.class);

	private static final String HTML_TARGET_CONTENT_DIV_SELECTOR = "div.content";
	private static final String TEAM_DIV_SELECTOR = "div.ply.name";
	private static final String DATE_AND_TIME_DIV_ATTRIBUTE = "data-esd";
	private static final String MATCH_ENTRY_CLASS_NAME = "row-gray";
	private static final String MATCH_SCORE_CLASS_NAME = "div.sco";

	public LiveScoreMatchParser() {
	}

	@Override
	public List<Match> parse(String content) {

		if (StringUtil.isBlank(content)) {
			throw new IllegalArgumentException("HTML containing the matches is invalid");
		}

		Element contentDiv = getContentDiv(content);
		List<Match> matches = new ArrayList<>();

		int totalMatches = 0;

		for (Element div : contentDiv.getAllElements()) {

			if (isMatchEntry(div)) {
				totalMatches++;

				try {
					Match m = parseMatch(div);
					matches.add(m);
				} catch (Exception e) {
					log.error("Failed to create match with div - {}",
							div.toString().replace(System.lineSeparator(), ""), e);
				}
			}
		}

		log.info("Matches Crawled: {}, Skipped : {}", matches.size(), totalMatches - matches.size());
		return matches;
	}

	private Element getContentDiv(String content) {
		return Jsoup.parse(content).select(HTML_TARGET_CONTENT_DIV_SELECTOR).first();
	}

	private boolean isMatchEntry(Element div) {
		return div.className().contains(MATCH_ENTRY_CLASS_NAME);
	}

	public Match parseMatch(Element div) throws ParseException {

		String homeTeam = getHomeTeam(div);
		String awayTeam = getAwayTeam(div);
		Date startDate = getStartDateAndTime(div);
		String score = getScore(div);

		Match m = new FootballMatchBuilder()
				.setHomeTeamName(homeTeam)
				.setAwayTeamName(awayTeam)
				.setStartDate(startDate)
				.setResult(score)
				.build();

		log.info("Successfully parsed - '{}'", m);
		return m;
	}

	private String getHomeTeam(Element div) {
		return getTeam(div, true);
	}

	private String getAwayTeam(Element div) {
		return getTeam(div, false);
	}

	private String getTeam(Element div, boolean isHomeTeam) {
		int index = isHomeTeam ? 0 : 1;
		return div.select(TEAM_DIV_SELECTOR).get(index).text().trim();
	}

	private Date getStartDateAndTime(Element div) throws ParseException {
		String dateAndTime = div.attr(DATE_AND_TIME_DIV_ATTRIBUTE);
		return DateFormats.LIVE_SCORE_MATCH_START_DATE_AND_TIME.parse(dateAndTime);
	}

	private String getScore(Element div) {
		return div.select(MATCH_SCORE_CLASS_NAME).first().text().trim();
	}
}
