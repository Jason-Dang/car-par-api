package com.jd.carparkapi.dto;

import java.time.LocalDateTime;

public record ParkingBillDTO(Long billId, String vehicleReg, Double vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {}