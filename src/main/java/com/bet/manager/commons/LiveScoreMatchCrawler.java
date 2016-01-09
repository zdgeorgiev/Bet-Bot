package com.bet.manager.commons;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveScoreMatchCrawler implements IMatchCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(LiveScoreMatchCrawler.class);

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MMMMM dd");

    private final String HTML_BODY_OPEN_TAG = "<body";
    private final String HTML_BODY_CLOSE_TAG = "</body";
    private final String HTML_TARGET_DIV_CONTENT_CLASS = "div.content";

    private final String START_DATE_MATCHES_CLASS_NAME = "tright fs11";
    private final String MATCH_ENTRY = "row-gray";

    @Override
    public List<Match> getMatches(URL page, IParserHandler handler) {

        String content = crawl(page);

        Document doc = Jsoup.parse(content);
        List<Match> matches = new ArrayList<>();

        Element contentDiv = doc.select(HTML_TARGET_DIV_CONTENT_CLASS).first();

        if (contentDiv == null) {
            throw new IllegalArgumentException("HTML content is invalid.");
        }

        String startDate = "";

        for (Element div : contentDiv.getAllElements()) {

            if (div.className().equals(START_DATE_MATCHES_CLASS_NAME)) {
                startDate = div.text();
            } else if (div.className().equals(MATCH_ENTRY)) {

                Match m;

                try {
                    m = handler.parse(div.toString(), DATE_FORMAT.parse(startDate));
                } catch (ParseException e) {
                    LOG.error("Cannot parse date - {}", startDate);
                    e.printStackTrace();
                    continue;
                }

                matches.add(m);
                LOG.info("Successfully created - {}", m);
            }
        }

        LOG.info("Done.. Total matches crawled: {}", matches.size());
        return matches;
    }

    @Override
    public void scheduleForNextExecution(Date date) {
        return;
    }

    private String crawl(URL page) {

        String content;
        try (InputStream is = new BufferedInputStream(page.openStream())) {
            content = IOUtils.toString(is, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read the url - " + page.toString());
        }

        int bodyOpenTagIndex = content.indexOf(HTML_BODY_OPEN_TAG);
        int bodyCloseTagIndex = content.indexOf(HTML_BODY_CLOSE_TAG);

        LOG.info("Successfully crawled url - {}", page.toString());
        return content.substring(bodyOpenTagIndex, bodyCloseTagIndex + HTML_BODY_CLOSE_TAG.length());
    }
}
