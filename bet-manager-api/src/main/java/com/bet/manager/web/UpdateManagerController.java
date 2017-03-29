package com.bet.manager.web;

import com.bet.manager.services.UpdateManagerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping(value = "/matches", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UpdateManagerController {

	@Autowired
	private UpdateManagerService updateManagerService;

	@RequestMapping(value = "/fetch", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Fetch matches from the source.")
	public void update() throws MalformedURLException, InterruptedException {

		updateManagerService.fetch();
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Create metadata for the matches that does not have already.")
	public void process() {

		updateManagerService.process();
	}

	@RequestMapping(value = "/predict", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Create predictions for the matches that are not predicted yet.")
	public void predict() {

		updateManagerService.predict();
	}
}
