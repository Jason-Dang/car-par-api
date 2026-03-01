package com.tds.carparkapi.ExceptionHandling.CustomExceptions;

import org.springframework.http.HttpStatus;

public class DatabaseConnectionException extends BaseCustomException {
    public DatabaseConnectionException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
