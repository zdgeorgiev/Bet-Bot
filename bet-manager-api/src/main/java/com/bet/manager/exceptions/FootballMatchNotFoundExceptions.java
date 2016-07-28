package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FootballMatchNotFoundExceptions extends RuntimeException {

	public FootballMatchNotFoundExceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public FootballMatchNotFoundExceptions(String message) {
		super(message);
	}
}
