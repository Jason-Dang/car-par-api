package com.jd.carparkapi.dto;

import java.time.LocalDateTime;

public record ParkResponse(Long spaceNumber, String vehicleReg, LocalDateTime timeIn) {}