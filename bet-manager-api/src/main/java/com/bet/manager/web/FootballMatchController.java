package com.bet.manager.web;

import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.services.FootballMatchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FootballMatchController {

	@Autowired
	private FootballMatchService footballMatchService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Create matches.")
	public void createMatches(
			@ApiParam(name = "matches", value = "Matches as list")
			@RequestBody List<FootballMatch> matches) {

		footballMatchService.createMatches(matches);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Update matches.")
	public void updateMatches(
			@ApiParam(name = "matches", value = "Matches as list")
			@RequestBody List<FootballMatch> matches) {

		footballMatchService.updateMatches(matches);
	}

	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Search for specific match.")
	public List<FootballMatch> retrieveMatches(
			@ApiParam(name = "team1", value = "First team name")
			@RequestParam(name = "team1", required = false) String team1,

			@ApiParam(name = "team2", value = "Second team name")
			@RequestParam(name = "team2", required = false) String team2,

			@ApiParam(name = "year", value = "Year of the match")
			@RequestParam(name = "year", required = false) Optional<Integer> year,

			@ApiParam(name = "round", value = "Round of the match")
			@RequestParam(name = "round", required = false) Optional<Integer> round,

			@ApiParam(name = "predictionType", value = "Prediction type of a match",
					defaultValue = "NOT_PREDICTED", allowableValues = "CORRECT, NOT_PREDICTED, INCORRECT")
			@RequestParam(name = "predictionType", required = false) String predictionType,

			@ApiParam(name = "matchStatus", value = "Status of a match",
					defaultValue = "NOT_STARTED", allowableValues = "NOT_STARTED, STARTED, FINISHED")
			@RequestParam(name = "matchStatus", required = false) String matchStatus,

			@ApiParam(name = "limit", value = "Limit of the result", defaultValue = "10")
			@RequestParam(name = "limit", defaultValue = "10", required = false) int limit,

			@ApiParam(name = "offset", value = "Offset of the result", defaultValue = "0")
			@RequestParam(name = "offset", defaultValue = "0", required = false) int offset) {

		return footballMatchService.retrieveMatches(team1, team2, year, round, predictionType, matchStatus, limit, offset);
	}

	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Return matches count in the database.")
	public int matchesCount() {

		return footballMatchService.matchesCount();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete all matches from the database.")
	public String deleteAll() {

		footballMatchService.deleteAll();
		return "All matches successfully deleted";
	}
}
