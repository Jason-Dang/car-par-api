package com.tds.carparkapi.DTO;

import java.time.LocalDateTime;

public class OccupiedParkingSpaceDTO {
    private Long spaceNumber;
    private String vehicleReg;
    private LocalDateTime timeIn;

    public OccupiedParkingSpaceDTO() {}

    public OccupiedParkingSpaceDTO(Long spaceNumber, String vehicleReg, LocalDateTime timeIn) {
        this.spaceNumber = spaceNumber;
        this.vehicleReg = vehicleReg;
        this.timeIn = timeIn;
    }

    public Long getSpaceNumber() {
        return this.spaceNumber;
    }

    public String getVehicleReg() {
        return this.vehicleReg;
    }

    public LocalDateTime getTimeIn() {
        return this.timeIn;
    }
}
