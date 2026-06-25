package com.jd.carparkapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ParkRequest(
    @NotBlank
    @Size(max = 10)
    String vehicleReg,
    Integer vehicleType
) {

}