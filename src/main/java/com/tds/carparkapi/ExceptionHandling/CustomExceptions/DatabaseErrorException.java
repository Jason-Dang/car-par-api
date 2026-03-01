package com.tds.carparkapi.ExceptionHandling.CustomExceptions;

import org.springframework.http.HttpStatus;

public class DatabaseErrorException extends BaseCustomException {
    public DatabaseErrorException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
