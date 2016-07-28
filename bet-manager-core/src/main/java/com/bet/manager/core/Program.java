package com.bet.manager.core;

import com.bet.manager.core.data.DataManager;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.util.FootballMatchBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Date;

public class Program {

	public static void main(String[] args) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		System.out.println(objectMapper.writeValueAsString(new FootballMatchBuilder()
				.setHomeTeamName("CSKA")
				.setAwayTeamName("LESVKI")
				.setPrediction("CSKA")
				.setYear(2011)
				.setRound(3)
				.build()));

		System.out.println(objectMapper.writeValueAsString(new FootballMatchBuilder()
				.setHomeTeamName("CSKA")
				.setAwayTeamName("LESVKI")
				.setPrediction("CSKA")
				.setResult("2-1")
				.setYear(2011)
				.setRound(3)
				.build()));

		System.out.println("===========================");

		FootballMatch match = new DataManager(false).createFootballMatch("FC Schalke 04", "SpVgg Greuther Fürth", 2012, 3);

		System.out.println(objectMapper.writeValueAsString(match));

		System.out.println("===========================");

		Thread.sleep(1000);

		FootballMatch match2 = new FootballMatchBuilder(match)
				.setStartDate(new Date())
				.build();

		System.out.println(objectMapper.writeValueAsString(match2));
	}
}
