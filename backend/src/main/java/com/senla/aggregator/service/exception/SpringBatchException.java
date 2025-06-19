package com.senla.aggregator.service.exception;

public class SpringBatchException extends RuntimeException {
    public SpringBatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
