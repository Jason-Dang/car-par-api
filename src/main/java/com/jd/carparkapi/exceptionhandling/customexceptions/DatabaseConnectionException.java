package com.jd.carparkapi.exceptionhandling.customexceptions;

import org.springframework.http.HttpStatus;

public class DatabaseConnectionException extends BaseCustomException {
    public DatabaseConnectionException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
