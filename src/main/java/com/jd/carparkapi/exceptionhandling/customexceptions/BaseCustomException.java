package com.jd.carparkapi.exceptionhandling.customexceptions;

import org.springframework.http.HttpStatus;

public class BaseCustomException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BaseCustomException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}
