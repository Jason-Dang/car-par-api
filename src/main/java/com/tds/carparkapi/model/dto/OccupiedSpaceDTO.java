package com.tds.carparkapi.model.dto;

import java.time.LocalDateTime;

public class OccupiedSpaceDTO {
    private Long spaceNumber;
    private String vehicleReg;
    private Integer vehicleType;
    private LocalDateTime timeIn;

    public OccupiedSpaceDTO() {}

    public OccupiedSpaceDTO(Long spaceNumber, String vehicleReg, Integer vehicleType, LocalDateTime timeIn) {
        this.spaceNumber = spaceNumber;
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = timeIn;
    }
}
