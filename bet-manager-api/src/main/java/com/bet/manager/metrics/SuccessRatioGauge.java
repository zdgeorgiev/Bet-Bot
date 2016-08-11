package com.bet.manager.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.RatioGauge;

public class SuccessRatioGauge extends RatioGauge {

	private final Counter success;
	private final Counter fail;

	public SuccessRatioGauge(Counter success, Counter fail) {
		this.success = success;
		this.fail = fail;
	}

	@Override
	protected Ratio getRatio() {
		long successes = this.success.getCount();
		long failures = this.fail.getCount();

		double nominator = (double) successes;
		double denominator = (double) (successes + failures);

		if (nominator == 0 && denominator == 0)
			nominator = 1;

		denominator = denominator == 0 ? 1 : denominator;
		return Ratio.of(nominator, denominator);
	}
}
