package com.tds.carparkapi.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vehicleReg;
    private Integer vehicleType;
    private LocalDateTime timeIn;

    protected Space() {}

    public Space(String vehicleReg, Integer vehicleType, LocalDateTime timeIn) {
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = timeIn;
    }
}
