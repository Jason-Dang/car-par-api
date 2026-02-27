package com.tds.carparkapi.service;

import com.tds.carparkapi.model.dto.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingBill;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.dto.ParkingBillDTO;
import com.tds.carparkapi.respository.ParkingBillRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ParkingService {
    @Autowired
    private ParkingBillRepository parkingBillRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    private ParkingBillDTO mapToParkingBillDTO(ParkingBill parkingBill) {
        return new ParkingBillDTO(
            parkingBill.getId(),
            parkingBill.getVehicleReg(),
            parkingBill.getVehicleCharge(),
            parkingBill.getTimeIn(),
            parkingBill.getTimeOut()
        );
    }

    private OccupiedParkingSpaceDTO mapToOccupiedParkingSpaceDTO(ParkingSpace parkingSpace) {
        return new OccupiedParkingSpaceDTO(
            parkingSpace.getId(),
            parkingSpace.getVehicleReg(),
            parkingSpace.getTimeIn()
        );
    }

    private Double getMinuteRate(Integer vehicleType) {
        if (vehicleType == 1) {
            return .1;
        }

        if (vehicleType == 2) {
            return .2;
        }

        return .4;
    }

    public Map<String, Integer> initialiseParkingSpacesInventory(HttpServletRequest request, Integer availableSpaces) {
        HttpSession session = request.getSession();

        if (session.getAttribute("availableSpaces") == null) {
            session.setAttribute("availableSpaces", availableSpaces);
        }

        if (session.getAttribute("occupiedSpaces") == null) {
            session.setAttribute("occupiedSpaces", 0);
        }

        return Map.of(
                "availableSpaces", Integer.parseInt(session.getAttribute("availableSpaces").toString()),
                "occupiedSpaces", Integer.parseInt(session.getAttribute("occupiedSpaces").toString())
        );
    }

    public ParkingSpacesInventoryDTO getParkingSpacesInventory(Integer availableSpaces, Integer occupiedSpaces) {
        return new ParkingSpacesInventoryDTO(availableSpaces, occupiedSpaces);
    }

    public OccupiedParkingSpaceDTO getNextAvailableParkingSpace(
        String vehicleReg,
        Integer vehicleType,
        HttpServletRequest request
    ) {
        ParkingSpace existingParkingSpace = parkingSpaceRepository.findOneByVehicleReg(vehicleReg);

        if (existingParkingSpace != null) {
            return mapToOccupiedParkingSpaceDTO(existingParkingSpace);
        }

        ParkingSpace parkingSpace = new ParkingSpace(vehicleReg, vehicleType, LocalDateTime.now());

        HttpSession session = request.getSession();
        session.setAttribute("availableSpaces", Integer.parseInt(session.getAttribute("availableSpaces").toString()) - 1);
        session.setAttribute("occupiedSpaces", Integer.parseInt(session.getAttribute("occupiedSpaces").toString()) + 1);

        ParkingSpace savedParkingSpace = parkingSpaceRepository.save(parkingSpace);

        return mapToOccupiedParkingSpaceDTO(savedParkingSpace);
    }

    public ParkingBillDTO getParkingBillForVehicleReg(String vehicleReg, HttpServletRequest request) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findOneByVehicleReg(vehicleReg);

        Integer vehicleType = parkingSpace.getVehicleType();
        LocalDateTime timeIn = parkingSpace.getTimeIn();

        parkingSpaceRepository.delete(parkingSpace);

        HttpSession session = request.getSession();
        session.setAttribute("availableSpaces", Integer.parseInt(session.getAttribute("availableSpaces").toString()) + 1);
        session.setAttribute("occupiedSpaces", Integer.parseInt(session.getAttribute("occupiedSpaces").toString()) - 1);

        LocalDateTime timeOut = LocalDateTime.now();
        Duration diff = Duration.between(timeIn, timeOut);

        long minutesStayed = diff.toMinutes();
        double surcharge = Math.floor(minutesStayed / 5.0);
        double minuteRate = getMinuteRate(vehicleType);
        BigDecimal bigDec = BigDecimal.valueOf((minutesStayed * minuteRate) + surcharge);
        bigDec = bigDec.setScale(2, RoundingMode.HALF_UP);
        double vehicleCharge = bigDec.doubleValue();

        ParkingBill parkingBill = new ParkingBill(vehicleReg, vehicleCharge, timeIn, timeOut);
        ParkingBill savedParkingBill = parkingBillRepository.save(parkingBill);

        return mapToParkingBillDTO(savedParkingBill);
    }
}
