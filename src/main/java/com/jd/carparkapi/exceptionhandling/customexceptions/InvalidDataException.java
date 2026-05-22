package com.jd.carparkapi.exceptionhandling.customexceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends BaseCustomException {
    public InvalidDataException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
