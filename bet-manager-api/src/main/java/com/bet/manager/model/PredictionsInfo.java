package com.bet.manager.model;

import java.io.Serializable;

public class PredictionsInfo implements Serializable {

	private static final long serialVersionUID = 1436475920667815001L;

	private String correctness;
	private String info;

	public PredictionsInfo() {

	}

	public PredictionsInfo(int correctOnes, int total) {

		this.correctness = String.format("%.2f%", correctOnes / (total * 1.0));
		this.info = String.format("Predicted %s of %s total", correctOnes, total);
	}

	public String getInfo() {
		return info;
	}

	public String getCorrectness() {
		return correctness;
	}
}
