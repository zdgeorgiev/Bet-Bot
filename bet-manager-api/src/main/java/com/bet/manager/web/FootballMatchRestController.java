package com.bet.manager.web;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.services.FootballMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FootballMatchRestController {

	@Autowired
	private FootballMatchService footballMatchService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public String createMatches(@RequestBody List<FootballMatch> matches) {

		int createdCount = footballMatchService.createMatches(matches);
		return String.format("Successfully created %s out of %s matches", createdCount, matches.size());
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public String updateMatches(@RequestBody List<FootballMatch> matches) {

		int updatedCount = footballMatchService.updateMatch(matches);
		return String.format("Successfully updated %s out of %s matches", updatedCount, matches.size());
	}

	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Collection<FootballMatch> retrieveAll() {

		return footballMatchService.retrieveAll();
	}

	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page<FootballMatch> retrieveMatches(
			@RequestParam(name = "team1", required = false) String team1,
			@RequestParam(name = "team2", required = false) String team2,
			@RequestParam(name = "year", required = false) Optional<Integer> year,
			@RequestParam(name = "round", required = false) Optional<Integer> round,
			@RequestParam(name = "correctPrediction", required = false) Optional<Boolean> correctPrediction,
			@RequestParam(name = "finished", required = false) Optional<Boolean> finished,
			@RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
			@RequestParam(name = "offset", defaultValue = "0", required = false) int offset) {

		return footballMatchService.retrieveMatches(team1, team2, year, round, correctPrediction, finished, limit, offset);
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
