package com.bet.manager.models.exceptions;

public class EmptyTeamNameException extends RuntimeException {
	public EmptyTeamNameException(String message) {
		super(message);
	}
}
