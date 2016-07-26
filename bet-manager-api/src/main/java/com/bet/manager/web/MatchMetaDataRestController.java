package com.bet.manager.web;

import com.bet.manager.model.dao.MatchMetaData;
import com.bet.manager.services.MatchMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page<MatchMetaData> retrieveMetaData(
			@RequestParam(name = "homeTeam") String homeTeam,
			@RequestParam(name = "awayTeam", required = false) String awayTeam,
			@RequestParam(name = "year", required = false) Optional<Integer> year,
			@RequestParam(name = "round", required = false) Optional<Integer> round,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
			@RequestParam(name = "offset", required = false, defaultValue = "0") int offset) {

		return matchMetaDataService.retrieveMetaData(homeTeam, awayTeam, year, round, limit, offset);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Collection<MatchMetaData> retrieveAll() {

		return matchMetaDataService.retrieveAll();
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
}