package com.bet.manager.model.exceptions;

public class EmptyTeamNameException extends RuntimeException {
	public EmptyTeamNameException(String message) {
		super(message);
	}
}
