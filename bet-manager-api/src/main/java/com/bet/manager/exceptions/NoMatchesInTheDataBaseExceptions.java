package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoMatchesInTheDataBaseExceptions extends RuntimeException {

	public NoMatchesInTheDataBaseExceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMatchesInTheDataBaseExceptions(String message) {
		super(message);
	}
}
