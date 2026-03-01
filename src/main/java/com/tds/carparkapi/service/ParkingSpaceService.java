package com.tds.carparkapi.service;

import com.tds.carparkapi.model.dto.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.model.dto.ParkingSpaceInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.entity.ParkingSpaceInventory;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParkingSpaceService {
    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    public ParkingSpaceService(
        ParkingSpaceRepository parkingSpaceRepository,
        ParkingSpaceInventoryRepository parkingSpaceInventoryRepository
    ) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceInventoryRepository = parkingSpaceInventoryRepository;
    }

    private ParkingSpaceInventoryDTO mapToParkingSpaceInventoryDTO(ParkingSpaceInventory parkingSpaceInventory) {
        return new ParkingSpaceInventoryDTO(
                parkingSpaceInventory.getAvailableSpaces(),
                parkingSpaceInventory.getOccupiedSpaces()
        );
    }

    private OccupiedParkingSpaceDTO mapToOccupiedParkingSpaceDTO(ParkingSpace parkingSpace) {
        return new OccupiedParkingSpaceDTO(
                parkingSpace.getId(),
                parkingSpace.getVehicleReg(),
                parkingSpace.getTimeIn()
        );
    }

    public void updateParkingSpaceInventory(boolean allocatedParkingSpace) {
        ParkingSpaceInventory parkingSpaceInventory = parkingSpaceInventoryRepository.findOneById(1L);

        parkingSpaceInventory.setAvailableSpaces(
                parkingSpaceInventory.getAvailableSpaces()
                        + (allocatedParkingSpace ? -1 : 1)
        );

        parkingSpaceInventory.setOccupiedSpaces(
                parkingSpaceInventory.getOccupiedSpaces()
                        + (allocatedParkingSpace ? 1 : -1)
        );

        parkingSpaceInventoryRepository.save(parkingSpaceInventory);
    }

    public ParkingSpaceInventoryDTO getParkingSpaceInventory() {
        return mapToParkingSpaceInventoryDTO(parkingSpaceInventoryRepository.findOneById(1L));
    }

    public OccupiedParkingSpaceDTO allocateNextAvailableParkingSpace(String vehicleReg, Integer vehicleType) {
        ParkingSpace availableParkingSpace = parkingSpaceRepository.findNextAvailableParkingSpace();

        if (availableParkingSpace == null || availableParkingSpace.getId() == null) {
            throw new RuntimeException();
        }

        availableParkingSpace.setVehicleReg(vehicleReg);
        availableParkingSpace.setVehicleType(vehicleType);
        availableParkingSpace.setTimeIn(LocalDateTime.now());
        ParkingSpace allocatedParkingSpace = parkingSpaceRepository.save(availableParkingSpace);

        if (allocatedParkingSpace.getId() == null) {
            throw new RuntimeException();
        }

        updateParkingSpaceInventory(true);

        return mapToOccupiedParkingSpaceDTO(allocatedParkingSpace);
    }

    public ParkingSpace getAllocatedParkingSpace(String vehicleReg) {
        return parkingSpaceRepository.findOneByVehicleReg(vehicleReg);
    }

    public void deallocateParkingSpaceForReg(ParkingSpace allocatedParkingSpace) {
        allocatedParkingSpace.setVehicleReg(null);
        allocatedParkingSpace.setVehicleType(null);
        allocatedParkingSpace.setTimeIn(null);
        parkingSpaceRepository.save(allocatedParkingSpace);

        if (allocatedParkingSpace.getId() == null) {
            throw new RuntimeException();
        }

        updateParkingSpaceInventory(false);
    }
}
