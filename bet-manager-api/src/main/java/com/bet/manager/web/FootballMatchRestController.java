package com.bet.manager.web;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.services.FootballMatchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FootballMatchRestController {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Autowired
	private FootballMatchService footballMatchService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createMatches(@RequestBody List<FootballMatch> matches) {

		footballMatchService.createMatches(matches);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void updateMatches(@RequestBody List<FootballMatch> matches) throws JsonProcessingException {

		footballMatchService.updateMatches(matches);
	}

	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<FootballMatch> retrieveMatches(
			@RequestParam(name = "team1", required = false) String team1,
			@RequestParam(name = "team2", required = false) String team2,
			@RequestParam(name = "year", required = false) Optional<Integer> year,
			@RequestParam(name = "round", required = false) Optional<Integer> round,
			@RequestParam(name = "predictionType", required = false) String predictionType,
			@RequestParam(name = "finished", required = false) Optional<Boolean> finished,
			@RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(name = "offset", defaultValue = "0", required = false) int offset) {

		return footballMatchService.retrieveMatches(team1, team2, year, round, predictionType, finished, limit, offset);
	}

	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public int matchesCount() {

		return footballMatchService.matchesCount();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public String deleteAll() {

		footballMatchService.deleteAll();
		return "All matches successfully deleted";
	}
}
