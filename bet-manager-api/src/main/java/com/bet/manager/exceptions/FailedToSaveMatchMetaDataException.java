package com.bet.manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FailedToSaveMatchMetaDataException extends RuntimeException {

	public FailedToSaveMatchMetaDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedToSaveMatchMetaDataException(String message) {
		super(message);
	}
}
