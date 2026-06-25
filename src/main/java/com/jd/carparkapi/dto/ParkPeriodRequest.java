package com.jd.carparkapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ParkPeriodRequest(
    @NotBlank
    @Size(max = 10)
    String vehicleReg,
    Integer vehicleType,
    LocalDateTime timeIn,
    LocalDateTime timeOut
) {

}