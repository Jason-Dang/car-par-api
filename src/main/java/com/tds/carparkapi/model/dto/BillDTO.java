package com.tds.carparkapi.model.dto;

import java.time.LocalDateTime;

public class BillDTO {
    private Long billId;
    private String vehicleReg;
    private Double vehicleCharge;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;

    public BillDTO() {}

    public BillDTO(String vehicleReg, Double vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.vehicleReg = vehicleReg;
        this.vehicleCharge = vehicleCharge;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
}
