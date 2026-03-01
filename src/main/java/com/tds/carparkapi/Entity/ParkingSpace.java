package com.tds.carparkapi.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicleReg", length=12)
    private String vehicleReg;

    @Column(name="vehicleType", length=1)
    private Integer vehicleType;

    @Column(name="timeIn", length=18)
    private LocalDateTime timeIn;

    public ParkingSpace() {}

    public ParkingSpace(String vehicleReg, Integer vehicleType, LocalDateTime timeIn) {
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = timeIn;
    }

    public Long getId() {
        return this.id;
    }

    public String getVehicleReg() {
        return this.vehicleReg;
    }

    public Integer getVehicleType() {
        return this.vehicleType;
    }

    public LocalDateTime getTimeIn() {
        return this.timeIn;
    }

    public void setVehicleReg(String vehicleReg) {
        this.vehicleReg = vehicleReg;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }
}
