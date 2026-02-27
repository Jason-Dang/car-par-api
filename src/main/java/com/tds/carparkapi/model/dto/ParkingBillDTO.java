package com.tds.carparkapi.model.dto;

import java.time.LocalDateTime;

public class ParkingBillDTO {
    private Long billId;
    private String vehicleReg;
    private Double vehicleCharge;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;

    public ParkingBillDTO() {}

    public ParkingBillDTO(Long id, String vehicleReg, Double vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.billId = id;
        this.vehicleReg = vehicleReg;
        this.vehicleCharge = vehicleCharge;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Long getBillId() {
        return this.billId;
    }

    public String getVehicleReg() {
        return this.vehicleReg;
    }

    public Double getVehicleCharge() {
        return this.vehicleCharge;
    }

    public LocalDateTime getTimeIn() {
        return this.timeIn;
    }

    public LocalDateTime getTimeOut() {
        return this.timeOut;
    }
}
