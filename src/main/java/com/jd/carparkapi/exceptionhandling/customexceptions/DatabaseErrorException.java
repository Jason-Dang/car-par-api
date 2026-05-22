package com.jd.carparkapi.exceptionhandling.customexceptions;

import org.springframework.http.HttpStatus;

public class DatabaseErrorException extends BaseCustomException {
    public DatabaseErrorException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
