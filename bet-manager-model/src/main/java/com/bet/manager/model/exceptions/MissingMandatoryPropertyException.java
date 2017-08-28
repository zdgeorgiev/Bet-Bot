package com.bet.manager.model.exceptions;

public class MissingMandatoryPropertyException extends RuntimeException {

	public MissingMandatoryPropertyException(String message) {
		super(message);
	}

	public MissingMandatoryPropertyException(String message, Throwable cause) {
		super(message, cause);
	}
}
