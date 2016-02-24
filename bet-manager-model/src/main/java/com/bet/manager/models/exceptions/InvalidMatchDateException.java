package com.bet.manager.models.exceptions;

public class InvalidMatchDateException extends RuntimeException {
	public InvalidMatchDateException(String message) {
		super(message);
	}
}