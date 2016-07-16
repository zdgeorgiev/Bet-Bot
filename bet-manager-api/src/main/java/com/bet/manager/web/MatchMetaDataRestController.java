package com.bet.manager.web;

import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.services.MatchMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/metadata", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MatchMetaDataRestController {

	@Autowired
	private MatchMetaDataService matchMetaDataService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String createEntries(@RequestBody List<MatchMetaData> entries) {

		int createdCount = matchMetaDataService.createEntries(entries);
		return "Successfully created " + createdCount + " out of " + entries.size() + " entries";
	}

	@ResponseBody
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Collection<MatchMetaData> retrieveMetaData(
			@RequestParam(name = "team") String team,
			@RequestParam(name = "opponent", required = false) String opponent,
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "round", required = false) Integer round,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {

		return matchMetaDataService.retrieveMetaData(team, opponent, year, round, limit, offset);
	}

	@ResponseBody
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public int metaDataCount() {

		return matchMetaDataService.metaDataCount();
	}

	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public String deleteAll() {

		matchMetaDataService.deleteAll();
		return "All meta data successfully deleted";
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FootballMatch test() {
		return matchMetaDataService.test();
	}
}