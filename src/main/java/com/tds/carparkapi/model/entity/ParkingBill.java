package com.tds.carparkapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class ParkingBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicleReg", length=50, nullable=false)
    private String vehicleReg;

    @Column(name="vehicleCharge", length=50, nullable=false)
    private Double vehicleCharge;

    @Column(name="timeIn", length=18, nullable=false)
    private LocalDateTime timeIn;

    @Column(name="timeOut", length=18, nullable=false)
    private LocalDateTime timeOut;

    protected ParkingBill() {}

    public ParkingBill(String vehicleReg, Double vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.vehicleReg = vehicleReg;
        this.vehicleCharge = vehicleCharge;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Long getId() {
        return this.id;
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
