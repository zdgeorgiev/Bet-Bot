package com.bet.manager.web;

import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.services.FootballMatchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Return matches count in the database.")
	public long matchesCount() {

		return footballMatchService.matchesCount();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete specific match from the database.")
	public void deleteMatch(
			@ApiParam(name = "match", value = "Match to delete.")
			@RequestBody FootballMatch match) {

		footballMatchService.deleteMatch(match);
	}

	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Delete all matches from the database.")
	public String deleteAll() {

		footballMatchService.deleteAll();
		return "All matches successfully deleted";
	}
}
