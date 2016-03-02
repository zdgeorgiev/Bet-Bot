package com.bet.manager.core;

import com.bet.manager.core.exceptions.DocumentParseException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class DocumentUtils {

	private static final DocumentBuilder dBuilder;
	private static final DocumentBuilderFactory dbFactory;

	static {
		dbFactory = DocumentBuilderFactory.newInstance();

		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException("Failed to create document builder.");
		}
	}

	private DocumentUtils() {
	}

	public static Document parse(String xml) {

		if (xml.contains("<!DOCTYPE sports-content SYSTEM \"../specification/dtd/sportsml-core.dtd\" >")) {
			xml = xml.replace("<!DOCTYPE sports-content SYSTEM \"../specification/dtd/sportsml-core.dtd\" >", "");
		}

		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xml));
		Document doc;

		try {
			doc = dBuilder.parse(is);
		} catch (SAXException | IOException e) {
			throw new DocumentParseException("Failed to parse document from xml.");
		}

		doc.getDocumentElement().normalize();
		return doc;
	}
}
