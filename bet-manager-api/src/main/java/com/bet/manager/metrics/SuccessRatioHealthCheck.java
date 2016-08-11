package com.bet.manager.metrics;

import com.codahale.metrics.RatioGauge;
import com.codahale.metrics.health.HealthCheck;

public class SuccessRatioHealthCheck extends HealthCheck {

	final RatioGauge successRatio;

	public SuccessRatioHealthCheck(RatioGauge successRatio) {
		this.successRatio = successRatio;
	}

	@Override
	protected Result check() throws Exception {
		double ratio = successRatio.getValue();
		if (ratio >= 0.2) {
			return Result.healthy("Success ratio is %s", ratio);
		}

		return Result.unhealthy("Bet manager api is UNHEALTHY, success ratio is %s", ratio);
	}
}
