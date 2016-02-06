package com.bet.manager.model.exceptions;

public class InvalidMatchDateException extends RuntimeException {
	public InvalidMatchDateException(String message) {
		super(message);
	}
}