package com.jd.carparkapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.time.LocalDateTime;

public record ErrorResponse(
    String errorCode,
    String message,
    int status,
    LocalDateTime timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL) String debugInfo
) {}