package com.jd.carparkapi.exceptionhandling.customexceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseCustomException {
    public ResourceNotFoundException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
