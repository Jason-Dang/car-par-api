package com.tds.carparkapi.ExceptionHandling;

import com.tds.carparkapi.DTO.ErrorResponseDTO;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.DatabaseConnectionException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.DatabaseErrorException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.InvalidDataException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value("${app.config.enableDebugging}") String enableDebugging;

    @ExceptionHandler(DatabaseConnectionException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseConnection(DatabaseConnectionException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(
            ex.getErrorCode(),
            ex.getMessage(),
            ex.getStatus().value(),
            Instant.now()
        ), ex.getStatus());
    }

    @ExceptionHandler(DatabaseErrorException.class)
    public ResponseEntity<ErrorResponseDTO> handleDatabaseError(DatabaseErrorException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                Instant.now()
        ), ex.getStatus());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleRInvalidData(InvalidDataException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                Instant.now()
        ), ex.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                Instant.now()
        ), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponseDTO(
                "",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
