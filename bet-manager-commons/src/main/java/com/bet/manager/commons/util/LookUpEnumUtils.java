package com.bet.manager.commons.util;

public class LookUpEnumUtils {

	public static <E extends Enum<E>> E lookup(Class<E> clazz, String value) {

		E result;

		try {
			result = Enum.valueOf(clazz, value.toUpperCase());
		} catch (Exception e) {

			throw new IllegalArgumentException(
					"Invalid value for enum " + clazz.getSimpleName() + ": " + value.toUpperCase());
		}

		return result;
	}
}