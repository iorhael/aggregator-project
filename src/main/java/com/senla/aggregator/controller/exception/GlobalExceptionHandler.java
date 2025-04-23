package com.senla.aggregator.controller.exception;

import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.service.exception.KeycloakException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.senla.aggregator.controller.helper.Messages.DATA_INTEGRITY_VIOLATION_EXCEPTION;
import static com.senla.aggregator.controller.helper.Messages.VALIDATION_ERROR_MESSAGE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            KeycloakException.class,
            ClientErrorException.class,
            HttpMessageNotReadableException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfoDto handleBadRequest(Exception exception) {
        String errorMessage = exception.getMessage();

        return logAndPrepareResponse(errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfoDto handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return logAndPrepareResponse(exception.getMessage(), DATA_INTEGRITY_VIOLATION_EXCEPTION, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfoDto handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> details = new HashMap<>();
        exception.getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        return logAndPrepareResponse(exception.getMessage(), VALIDATION_ERROR_MESSAGE, details);
    }

    @ExceptionHandler({
            NotFoundException.class,
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseInfoDto handleNotFound(Exception exception) {
        String errorMessage = exception.getMessage();

        return logAndPrepareResponse(errorMessage);
    }

    private ResponseInfoDto logAndPrepareResponse(String logMessage,
                                                  String clientMessage,
                                                  Map<String, String> details) {
        log.error(logMessage);

        return ResponseInfoDto.builder()
                .message(clientMessage)
                .details(details)
                .build();
    }

    private ResponseInfoDto logAndPrepareResponse(String errorMessage) {
        return logAndPrepareResponse(errorMessage, errorMessage, Map.of());
    }
}
