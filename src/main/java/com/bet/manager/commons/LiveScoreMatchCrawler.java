package com.bet.manager.commons;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LiveScoreMatchCrawler implements IMatchCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(LiveScoreMatchCrawler.class);

    private final String HTML_BODY_OPEN_TAG = "<body";
    private final String HTML_BODY_CLOSE_TAG = "</body";
    private final String HTML_TARGET_CONTENT_DIV_SELECTOR = "div.content";

    private final String START_DATE_MATCHES_CLASS_NAME = "tright fs11";
    private final String MATCH_ENTRY_CLASS_NAME = "row-gray";

    private IParserHandler parserHandler;

    public LiveScoreMatchCrawler(IParserHandler parserHandler) {
        this.parserHandler = parserHandler;
    }

    @Override
    public List<Match> getMatches(String content) {

        Document doc = Jsoup.parse(content);
        List<Match> matches = new ArrayList<>();

        Element contentDiv = doc.select(HTML_TARGET_CONTENT_DIV_SELECTOR).first();

        if (StringUtil.isBlank(content)) {
            throw new IllegalArgumentException("HTML content is invalid");
        }

        String startDate = "";

        for (Element div : contentDiv.getAllElements()) {

            if (div.className().equals(START_DATE_MATCHES_CLASS_NAME)) {
                startDate = div.text();
            } else if (div.className().contains(MATCH_ENTRY_CLASS_NAME)) {

                Match m = parserHandler.parse(div.toString(), startDate);

                matches.add(m);
            }
        }

        LOG.info("Done.. Total matches crawled: {}", matches.size());
        return matches;
    }

    @Override
    public String crawl(URL page) {

        if (page == null) {
            throw new IllegalArgumentException("URL page handler cannot be null");
        }

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

    @Override
    public void scheduleForNextExecution(Date date) {
        throw new NotImplementedException();
    }
}
