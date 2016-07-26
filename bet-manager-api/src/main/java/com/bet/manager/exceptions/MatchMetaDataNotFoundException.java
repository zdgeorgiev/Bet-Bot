package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MatchMetaDataNotFoundException extends RuntimeException {

	public MatchMetaDataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MatchMetaDataNotFoundException(String message) {
		super(message);
	}
}
