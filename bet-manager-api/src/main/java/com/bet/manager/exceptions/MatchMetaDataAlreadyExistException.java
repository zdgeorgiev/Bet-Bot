package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MatchMetaDataAlreadyExistException extends RuntimeException {

	public MatchMetaDataAlreadyExistException(String message) {
		super(message);
	}

	public MatchMetaDataAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}
}
