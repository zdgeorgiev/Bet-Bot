package com.bet.manager.core.exceptions;

public class DataCreationException extends RuntimeException {

	public DataCreationException(String message) {
		super(message);
	}

	public DataCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
