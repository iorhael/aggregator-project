package com.senla.aggregator.service.exception;

public class GmailException extends RuntimeException {
    public GmailException(String message) {
        super(message);
    }

    public GmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
