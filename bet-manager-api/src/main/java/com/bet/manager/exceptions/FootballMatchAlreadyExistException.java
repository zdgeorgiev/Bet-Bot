package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FootballMatchAlreadyExistException extends RuntimeException {

	public FootballMatchAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public FootballMatchAlreadyExistException(String message) {
		super(message);
	}
}
