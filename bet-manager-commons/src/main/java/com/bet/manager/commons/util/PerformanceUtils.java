package com.bet.manager.commons.util;

import java.util.concurrent.TimeUnit;

public class PerformanceUtils {

	private PerformanceUtils() {
	}

	public static String convertToHumanReadable(long milliseconds) {
		String elapsed;

		long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
		long hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));

		if (days == 0) {
			elapsed = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
		} else {
			elapsed = String.format("%dd%02dh:%02dm:%02ds", days, hours, minutes, seconds);
		}

		return elapsed;
	}
}
