package com.bet.manager.commons;

import com.bet.manager.utils.LiveScoreMatchUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LiveScoreMatchParser implements IMatchParser {

    private static final Logger log = LoggerFactory.getLogger(LiveScoreMatchParser.class);

    private final String HTML_TARGET_CONTENT_DIV_SELECTOR = "div.content";

    private final String START_DATE_MATCHES_CLASS_NAME = "tright fs11";
    private final String MATCH_ENTRY_CLASS_NAME = "row-gray";

    public LiveScoreMatchParser() {

    }

    @Override
    public List<Match> parseAll(String content) {

        if (StringUtil.isBlank(content)) {
            throw new IllegalArgumentException("HTML containing the matches is invalid");
        }

        Element contentDiv = getContentDiv(content);
        String startDate = "";
        List<Match> matches = new ArrayList<>();

        for (Element div : contentDiv.getAllElements()) {

            if (isDateClass(div)) {

                startDate = div.text();
            } else if (isMatchClass(div)) {

                addMatch(div, startDate, matches);
            }
        }

        log.info("Done.. Total matches crawled: {}", matches.size());
        return matches;
    }

    private Element getContentDiv(String content) {
        return Jsoup.parse(content).select(HTML_TARGET_CONTENT_DIV_SELECTOR).first();
    }

    private boolean isDateClass(Element div) {
        return div.className().equals(START_DATE_MATCHES_CLASS_NAME);
    }

    private boolean isMatchClass(Element div) {
        return div.className().contains(MATCH_ENTRY_CLASS_NAME);
    }

    private void addMatch(Element div, String startDate, List<Match> matches) {
        Match m = LiveScoreMatchUtils.parse(div, startDate);

        if (m != null) {
            matches.add(m);
        }
    }
}
