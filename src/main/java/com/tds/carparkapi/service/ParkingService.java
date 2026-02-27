package com.tds.carparkapi.service;

import com.tds.carparkapi.model.dto.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingBill;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.dto.ParkingBillDTO;
import com.tds.carparkapi.respository.ParkingBillRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public ParkingSpacesInventoryDTO getParkingSpacesInventory() {
        Integer availableSpaces = 0;
        Integer occupiedSpaces = 0;

        return new ParkingSpacesInventoryDTO(availableSpaces, occupiedSpaces);
    }

    public OccupiedParkingSpaceDTO getNextAvailableParkingSpace(String vehicleReg, Integer vehicleType) {
        ParkingSpace parkingSpace = new ParkingSpace(vehicleReg, vehicleType, LocalDateTime.now());

        ParkingSpace savedParkingSpace = parkingSpaceRepository.save(parkingSpace);

        return mapToOccupiedParkingSpaceDTO(savedParkingSpace);
    }

    public ParkingBillDTO getParkingBillForVehicleReg(String vehicleReg) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findByVehicleReg(vehicleReg);

        LocalDateTime timeIn = parkingSpace.getTimeIn();
        Double vehicleCharge = 0d;

        ParkingBill parkingBill = new ParkingBill(vehicleReg, vehicleCharge, timeIn, LocalDateTime.now());
        ParkingBill savedParkingBill = parkingBillRepository.save(parkingBill);

        return mapToParkingBillDTO(savedParkingBill);
    }
}
