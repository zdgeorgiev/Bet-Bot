package com.bet.manager.exceptions;

public class FailedToSaveMatchMetaDataException extends RuntimeException {

	public FailedToSaveMatchMetaDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedToSaveMatchMetaDataException(String message) {
		super(message);
	}
}
