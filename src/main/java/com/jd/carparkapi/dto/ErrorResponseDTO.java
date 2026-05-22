package com.jd.carparkapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

public record ErrorResponseDTO(
    String errorCode,
    String message,
    int status,
    Instant timestamp,
    @JsonInclude(JsonInclude.Include.NON_NULL) String debugInfo
) {}