package com.bet.manager.model.exceptions;

public class InvalidMetaDataConstructorCountException extends RuntimeException {

	public InvalidMetaDataConstructorCountException(String message) {
		super(message);
	}

	public InvalidMetaDataConstructorCountException(String message, Throwable cause) {
		super(message, cause);
	}
}
