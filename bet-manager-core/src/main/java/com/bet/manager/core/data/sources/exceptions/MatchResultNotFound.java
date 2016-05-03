package com.bet.manager.core.data.sources.exceptions;

public class MatchResultNotFound extends RuntimeException {
    public MatchResultNotFound() {
    }

    public MatchResultNotFound(String message) {
        super(message);
    }
}