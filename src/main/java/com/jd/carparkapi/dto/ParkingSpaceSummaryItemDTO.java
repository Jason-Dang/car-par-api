package com.jd.carparkapi.dto;

public class ParkingSpaceSummaryItemDTO {
    private final String vehicleReg;
    private final Integer stayDuration;

    public ParkingSpaceSummaryItemDTO(String vehicleReg, Integer stayDuration) {
        this.vehicleReg = vehicleReg;
        this.stayDuration = stayDuration;
    }

    public String getVehicleReg() {
        return this.vehicleReg;
    }

    public Integer getStayDuration() {
        return this.stayDuration;
    }
}
