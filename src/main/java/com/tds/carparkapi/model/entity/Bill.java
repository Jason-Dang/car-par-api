package com.tds.carparkapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vehicleReg;
    private Double vehicleCharge;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;

    protected Bill() {}

    public Bill(String vehicleReg, Double vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.vehicleReg = vehicleReg;
        this.vehicleCharge = vehicleCharge;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }
}
