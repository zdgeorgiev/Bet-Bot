package com.bet.manager.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Map;

public class DocumentUtils {

	private static final Logger log = LoggerFactory.getLogger(DocumentUtils.class);

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
		return parse(xml, Collections.emptyMap());
	}

	public static Document parse(String xml, Map<String, Document> parsedDocuments) {
		if (parsedDocuments.containsKey(xml)) {
			Document d = parsedDocuments.get(xml);
			log.info("Returning document cache version of {}", parsedDocuments.get(xml).getDocumentURI());
			return d;
		}

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

		try {
			parsedDocuments.put(doc.getTextContent(), doc);
		} catch (Exception e) {
			// This catch block is leaved empty not incidentally.
			// If the collection is Collections.emptyMap() items cannot be added and will throw exception
		}

		return doc;
	}

	private static class DocumentParseException extends RuntimeException {
		public DocumentParseException(String message) {
			super(message);
		}
	}
}

