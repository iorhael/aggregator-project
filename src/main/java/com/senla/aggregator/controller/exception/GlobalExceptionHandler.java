package com.senla.aggregator.controller.exception;

import com.senla.aggregator.controller.ControllerMessages;
import com.senla.aggregator.dto.ResponseInfoDto;
import com.senla.aggregator.service.exception.KeycloakException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.senla.aggregator.controller.ControllerMessages.VALIDATION_ERROR_MESSAGE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            KeycloakException.class,
            ClientErrorException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfoDto handleBadRequest(Exception exception) {
        String errorMessage = exception.getMessage();

        log.error(errorMessage);

        return ResponseInfoDto.builder()
                .message(errorMessage)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfoDto handleValidationErrors(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());

        Map<String, String> details = new HashMap<>();
        exception.getFieldErrors()
                .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

        return ResponseInfoDto.builder()
                .message(VALIDATION_ERROR_MESSAGE)
                .details(details)
                .build();
    }

    @ExceptionHandler({
            NotFoundException.class,
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseInfoDto handleNotFound(Exception exception) {
        String errorMessage = exception.getMessage();

        log.error(errorMessage);

        return ResponseInfoDto.builder()
                .message(errorMessage)
                .build();
    }
}
