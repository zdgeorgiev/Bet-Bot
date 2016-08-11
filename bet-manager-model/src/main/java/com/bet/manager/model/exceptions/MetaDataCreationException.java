package com.bet.manager.model.exceptions;

public class MetaDataCreationException extends RuntimeException {

	public MetaDataCreationException(String message) {
		super(message);
	}

	public MetaDataCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
