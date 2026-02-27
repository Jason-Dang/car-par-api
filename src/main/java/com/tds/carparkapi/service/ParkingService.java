package com.tds.carparkapi.service;

import com.tds.carparkapi.model.dto.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingBill;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.dto.ParkingBillDTO;
import com.tds.carparkapi.model.entity.ParkingSpacesInventory;
import com.tds.carparkapi.respository.ParkingBillRepository;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingService {
    @Autowired
    private ParkingBillRepository parkingBillRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

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

    private ParkingSpacesInventoryDTO mapToParkingSpacesInventoryDTO(ParkingSpacesInventory parkingSpacesInventory) {
        return new ParkingSpacesInventoryDTO(
                parkingSpacesInventory.getAvailableSpaces(),
                parkingSpacesInventory.getOccupiedSpaces()
        );
    }

    private void updateParkingSpaceInventory(boolean allocatedParkingSpace) {
        ParkingSpacesInventory parkingSpacesInventory = parkingSpaceInventoryRepository.findOneById(1L);

        parkingSpacesInventory.setAvailableSpaces(
                parkingSpacesInventory.getAvailableSpaces()
                + (allocatedParkingSpace ? -1 : 1)
        );

        parkingSpacesInventory.setOccupiedSpaces(
                parkingSpacesInventory.getOccupiedSpaces()
                + (allocatedParkingSpace ? 1 : -1)
        );

        parkingSpaceInventoryRepository.save(parkingSpacesInventory);
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

    public ParkingSpace getAllocatedParkingSpaceForVehicleReg(String vehicleReg) {
        return parkingSpaceRepository.findOneByVehicleReg(vehicleReg);
    }

    public ParkingSpacesInventoryDTO getParkingSpacesInventory() {
        return mapToParkingSpacesInventoryDTO(parkingSpaceInventoryRepository.findOneById(1L));
    }

    public OccupiedParkingSpaceDTO getNextAvailableParkingSpace(String vehicleReg, Integer vehicleType) {
        ParkingSpace availableParkingSpace = parkingSpaceRepository.findNextAvailableParkingSpace();
        availableParkingSpace.setVehicleReg(vehicleReg);
        availableParkingSpace.setVehicleType(vehicleType);
        availableParkingSpace.setTimeIn(LocalDateTime.now());
        ParkingSpace allocatedParkingSpace = parkingSpaceRepository.save(availableParkingSpace);

        updateParkingSpaceInventory(true);

        return mapToOccupiedParkingSpaceDTO(allocatedParkingSpace);
    }

    public ParkingBillDTO getParkingBillForVehicleReg(ParkingSpace allocatedParkingSpace) {
        LocalDateTime timeOut = LocalDateTime.now();

        String vehicleReg = allocatedParkingSpace.getVehicleReg();
        Integer vehicleType = allocatedParkingSpace.getVehicleType();
        LocalDateTime timeIn = allocatedParkingSpace.getTimeIn();

        allocatedParkingSpace.setVehicleReg(null);
        allocatedParkingSpace.setVehicleType(null);
        allocatedParkingSpace.setTimeIn(null);
        parkingSpaceRepository.save(allocatedParkingSpace);

        updateParkingSpaceInventory(false);

        Duration diff = Duration.between(timeIn, timeOut);
        long minutesStayed = diff.toMinutes();
        double surcharge = Math.floor(minutesStayed / 5.0);
        double minuteRate = getMinuteRate(vehicleType);
        BigDecimal vehicleCharge = BigDecimal.valueOf(
                (minutesStayed * minuteRate) + surcharge
        ).setScale(2, RoundingMode.HALF_UP);

        return mapToParkingBillDTO(parkingBillRepository.save(
            new ParkingBill(vehicleReg, vehicleCharge.doubleValue(), timeIn, timeOut)
        ));
    }
}
