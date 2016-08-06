package com.bet.manager.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class PredictionsInfo implements Serializable {

	private static final long serialVersionUID = 1436475920667815001L;

	private static final Logger logger = LoggerFactory.getLogger(PredictionsInfo.class);

	private String correctness;
	private int correct;
	private int total;
	private String info;

	public PredictionsInfo(int correctOnes, int totalPredicted) {

		if (totalPredicted != 0) {
			this.correctness = String.format("%.2f%s", (correctOnes / (totalPredicted * 1.0) * 100), "%");
		} else {
			this.correctness = "0.00%";
			logger.warn("Predicted matches may not be finished yet or no predictions at all");
		}

		this.correct = correctOnes;
		this.total = totalPredicted;

		this.info = String.format("Correct predictions %s of %s", correctOnes, totalPredicted);
	}

	public String getInfo() {
		return info;
	}

	public int getCorrect() {
		return correct;
	}

	public int getTotal() {
		return total;
	}

	public String getCorrectness() {
		return correctness;
	}
}
