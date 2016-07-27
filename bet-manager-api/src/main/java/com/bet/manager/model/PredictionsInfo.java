package com.bet.manager.model;

import java.io.Serializable;

public class PredictionsInfo implements Serializable {

	private static final long serialVersionUID = 1436475920667815001L;

	private String correctness;
	private String info;

	public PredictionsInfo(int correctOnes, int total) {

		this.correctness = String.format("%.2f%s", (correctOnes / (total * 1.0) * 100), "%");
		this.info = String.format("Correct predictions %s of %s", correctOnes, total);
	}

	public String getInfo() {
		return info;
	}

	public String getCorrectness() {
		return correctness;
	}
}
