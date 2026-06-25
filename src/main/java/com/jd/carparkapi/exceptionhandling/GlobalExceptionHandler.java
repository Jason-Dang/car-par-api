package com.jd.carparkapi.exceptionhandling;

import com.jd.carparkapi.dto.ErrorResponse;
import com.jd.carparkapi.exceptionhandling.customexceptions.BaseCustomException;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseConnectionException;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseErrorException;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.exceptionhandling.customexceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${app.config.enableDebugging}")
    boolean enableDebugging;

    private ErrorResponse buildError(BaseCustomException ex) {
        String debugInfo = enableDebugging
            ? ex.getClass().getSimpleName() + ": " + ex.getMessage()
            : null;
        return new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            ex.getStatus().value(),
            LocalDateTime.now(ZoneOffset.UTC),
            debugInfo
        );
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseConnection(DatabaseConnectionException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseError(DatabaseErrorException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidData(InvalidDataException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }
}