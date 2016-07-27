package com.bet.manager.core;

import com.bet.manager.core.data.DataManager;
import com.bet.manager.model.dao.MatchMetaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {

	private static Logger logger = LoggerFactory.getLogger(Program.class);

	public static void main(String[] args) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		MatchMetaData a = new DataManager(false).getDataForMatch("FC Schalke 04", "SpVgg Greuther Fürth", 2012, 3);
		System.out.println(objectMapper.writeValueAsString(a));
	}
}
