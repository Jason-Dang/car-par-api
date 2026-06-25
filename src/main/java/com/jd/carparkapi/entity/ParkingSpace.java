package com.jd.carparkapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicleReg", length=12)
    private String vehicleReg;

    @Column(name="vehicleType", length=1)
    private Integer vehicleType;

    @Column(name="timeIn", length=19)
    private LocalDateTime timeIn;

    @Column(name="timeOut", length=19)
    private LocalDateTime timeOut;
}
