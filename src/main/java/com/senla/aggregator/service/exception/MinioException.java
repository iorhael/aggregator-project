package com.senla.aggregator.service.exception;

public class MinioException extends RuntimeException {
    public MinioException(String message) {
        super(message);
    }

    public MinioException(String message, Throwable cause) {
        super(message, cause);
    }
}
