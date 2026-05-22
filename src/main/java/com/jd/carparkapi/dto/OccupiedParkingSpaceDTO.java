package com.jd.carparkapi.dto;

import java.time.LocalDateTime;

public record OccupiedParkingSpaceDTO(Long spaceNumber, String vehicleReg, LocalDateTime timeIn) {}