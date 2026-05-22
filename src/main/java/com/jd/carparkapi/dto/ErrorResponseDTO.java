package com.jd.carparkapi.dto;

import java.time.Instant;

public record ErrorResponseDTO(String errorCode, String message, int status, Instant timestamp) {
}
