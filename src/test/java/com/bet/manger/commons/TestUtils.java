package com.bet.manger.commons;

import org.apache.commons.io.IOUtils;
import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class TestUtils {

    public static String getResource(String path, Class c) {

        String content = "";

        try (InputStream is = c.getClassLoader().getResourceAsStream(path)) {
            content = IOUtils.toString(is, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read the resource " + path);
        }

        if (StringUtil.isBlank(content)) {
            throw new IllegalStateException("Resource " + path + " content cannot be empty.");
        }

        return content;
    }
}
