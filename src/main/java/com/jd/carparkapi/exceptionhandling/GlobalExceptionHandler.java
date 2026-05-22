package com.jd.carparkapi.exceptionhandling;

import com.jd.carparkapi.dto.ErrorResponseDTO;
import com.jd.carparkapi.exceptionhandling.customexceptions.BaseCustomException;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseConnectionException;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseErrorException;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.exceptionhandling.customexceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${app.config.enableDebugging}")
    boolean enableDebugging;

    private ErrorResponseDTO buildError(BaseCustomException ex) {
        String debugInfo = enableDebugging
            ? ex.getClass().getSimpleName() + ": " + ex.getMessage()
            : null;
        return new ErrorResponseDTO(
            ex.getErrorCode(),
            ex.getMessage(),
            ex.getStatus().value(),
            Instant.now(),
            debugInfo
        );
    }

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseConnection(DatabaseConnectionException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseError(DatabaseErrorException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidData(InvalidDataException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(buildError(ex), ex.getStatus());
    }
}