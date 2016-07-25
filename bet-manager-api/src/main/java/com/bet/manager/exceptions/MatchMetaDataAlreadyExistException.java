package com.bet.manager.exceptions;

public class MatchMetaDataAlreadyExistException extends RuntimeException {

	public MatchMetaDataAlreadyExistException(String message) {
		super(message);
	}

	public MatchMetaDataAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
