package com.bet.manager.web;

import com.bet.manager.services.MatchMetaDataService;
import com.bet.manager.web.model.MatchMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/metadata")
public class MatchMetaDataRestController {

	@Autowired
	private MatchMetaDataService matchMetaDataService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String createEnty(@RequestBody MatchMetaData entry) {

		matchMetaDataService.createEntry(entry);
		return "Successfully created 1 meta data";
	}

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
			@RequestParam(value = "team") String team,
			@RequestParam(value = "opponent", required = false) String opponent,
			@RequestParam(value = "year", required = false) int year,
			@RequestParam(value = "round", required = false) int round,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) {

		return matchMetaDataService.retrieveMetaData(team, opponent, year, round, limit, offset);
	}

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
}