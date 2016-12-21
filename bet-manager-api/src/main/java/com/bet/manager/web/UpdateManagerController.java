package com.bet.manager.web;

import com.bet.manager.services.UpdateManagerService;
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
	public void update() throws MalformedURLException, InterruptedException {
		updateManagerService.fetch();
	}

	@RequestMapping(value = "/process", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void process() {
		updateManagerService.process();
	}

	@RequestMapping(value = "/predict", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void predict() {
		updateManagerService.predict();
	}
}
