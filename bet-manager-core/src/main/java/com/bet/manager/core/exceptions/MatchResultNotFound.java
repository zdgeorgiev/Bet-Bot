package com.bet.manager.core.exceptions;

public class MatchResultNotFound extends RuntimeException {

	public MatchResultNotFound() {
	}

	public MatchResultNotFound(String message) {
		super(message);
	}
}