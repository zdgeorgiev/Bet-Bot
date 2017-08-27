package com.bet.manager.web;

import com.bet.manager.model.PredictionsInfo;
import com.bet.manager.services.PredictionInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/predictions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PredictionInfoController {

	@Autowired
	private PredictionInfoService predictionInfoService;

	@ResponseBody
	@RequestMapping(value = "/info/correct", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get number of correctly predicted matches.")
	public int getIncorrectMatchesCount() {
		return predictionInfoService.correctPredictedMatchesCount();
	}

	@ResponseBody
	@RequestMapping(value = "/info/incorrect", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get number of not correctly predicted matches.")
	public int getCorrectMatchesCount() {
		return predictionInfoService.incorrectPredictedMatchesCount();
	}

	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get predictions statistics.")
	public PredictionsInfo getPredictionsInfo() {
		return predictionInfoService.getPredictionsInfo();
	}
}
