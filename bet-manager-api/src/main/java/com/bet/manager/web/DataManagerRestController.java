package com.bet.manager.web;

import com.bet.manager.services.DataManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataManagerRestController {

	@Autowired
	private DataManagerService dataManagerService;

	@RequestMapping(value = "/process", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getDataForMatch(
			@RequestParam(name = "firstTeam") String firstTeam,
			@RequestParam(name = "secondTeam") String secondTeam,
			@RequestParam(name = "year") int year,
			@RequestParam(name = "round") int round) throws Exception {

		return dataManagerService.getDataForMatch(firstTeam, secondTeam, year, round);
	}
}
