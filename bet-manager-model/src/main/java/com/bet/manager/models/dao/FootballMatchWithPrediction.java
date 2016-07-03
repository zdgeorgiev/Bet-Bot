package com.bet.manager.models.dao;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class FootballMatchWithPrediction extends FootballMatch {

	@Column(name = "prediction")
	private String prediction;

	public FootballMatchWithPrediction() {
		this.setResultDelimiter("-");
	}

	public String getPredictionType() {
		return prediction;
	}

	public void setPredictionType(String predictionType) {
		this.prediction = predictionType;
	}
}
