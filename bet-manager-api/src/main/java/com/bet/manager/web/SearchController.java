package com.bet.manager.web;

import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import com.bet.manager.services.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SearchController {

	@Autowired
	private SearchService searchService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Search for specific matches.")
	public List<FootballMatch> retrieveMatches(
			@ApiParam(name = "homeTeam", value = "First team name")
			@RequestParam(name = "homeTeam", required = false) String homeTeam,

			@ApiParam(name = "awayTeam", value = "Second team name")
			@RequestParam(name = "awayTeam", required = false) String awayTeam,

			@ApiParam(name = "year", value = "Year of the match")
			@RequestParam(name = "year", required = false) Integer year,

			@ApiParam(name = "round", value = "Round of the match")
			@RequestParam(name = "round", required = false) Integer round,

			@ApiParam(name = "predictionType", value = "Prediction type of a match", allowableValues = "CORRECT, NOT_PREDICTED, INCORRECT")
			@RequestParam(name = "predictionType", required = false) PredictionType predictionType,

			@ApiParam(name = "matchStatus", value = "Status of a match", allowableValues = "NOT_STARTED, STARTED, FINISHED")
			@RequestParam(name = "matchStatus", required = false) MatchStatus matchStatus,

			@ApiParam(name = "limit", value = "Limit of the result", defaultValue = "10")
			@RequestParam(name = "limit", defaultValue = "10", required = false) int limit,

			@ApiParam(name = "offset", value = "Offset of the result", defaultValue = "0")
			@RequestParam(name = "offset", defaultValue = "0", required = false) int offset) {

		return searchService.retrieveMatches(homeTeam, awayTeam, year, round, predictionType, matchStatus, limit, offset);
	}
}
