package com.tds.carparkapi.Service;

import com.tds.carparkapi.DTO.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.DTO.ParkingSpaceInventoryDTO;
import com.tds.carparkapi.Entity.ParkingSpace;
import com.tds.carparkapi.Entity.ParkingSpaceInventory;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.DatabaseConnectionException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.DatabaseErrorException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.ResourceNotFoundException;
import com.tds.carparkapi.Respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.Respository.ParkingSpaceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        if (parkingSpaceInventory == null) {
            throw new ResourceNotFoundException("No parking space inventory found", "err-ps1", HttpStatus.NOT_FOUND);
        }

        parkingSpaceInventory.setAvailableSpaces(
                parkingSpaceInventory.getAvailableSpaces()
                        + (allocatedParkingSpace ? -1 : 1)
        );

        parkingSpaceInventory.setOccupiedSpaces(
                parkingSpaceInventory.getOccupiedSpaces()
                        + (allocatedParkingSpace ? 1 : -1)
        );

        try {
            parkingSpaceInventoryRepository.save(parkingSpaceInventory);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Unable to update parking space inventory in the database", "err-db1" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ParkingSpaceInventoryDTO getParkingSpaceInventory() {
        ParkingSpaceInventory parkingSpaceInventory = parkingSpaceInventoryRepository.findOneById(1L);

        if (parkingSpaceInventory == null) {
            throw new ResourceNotFoundException("No parking space inventory found", "err-ps1", HttpStatus.NOT_FOUND);
        }

        return mapToParkingSpaceInventoryDTO(parkingSpaceInventory);
    }

    public OccupiedParkingSpaceDTO allocateNextAvailableParkingSpace(String vehicleReg, Integer vehicleType) {
        ParkingSpace availableParkingSpace = parkingSpaceRepository.findNextAvailableParkingSpace();

        if (availableParkingSpace == null || availableParkingSpace.getId() == null) {
            throw new ResourceNotFoundException("No available parking space found", "err-ps3", HttpStatus.NOT_FOUND);
        }

        availableParkingSpace.setVehicleReg(vehicleReg);
        availableParkingSpace.setVehicleType(vehicleType);
        availableParkingSpace.setTimeIn(LocalDateTime.now());

        try {
            ParkingSpace allocatedParkingSpace = parkingSpaceRepository.save(availableParkingSpace);

            if (allocatedParkingSpace.getId() == null) {
                throw new DatabaseErrorException("Unable to save allocated parking space", "err-db2", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            updateParkingSpaceInventory(true);

            return mapToOccupiedParkingSpaceDTO(allocatedParkingSpace);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Unable to save allocated parking space in the database", "err-db1" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ParkingSpace getAllocatedParkingSpace(String vehicleReg) {
        return parkingSpaceRepository.findOneByVehicleReg(vehicleReg);
    }

    public void deallocateParkingSpaceForReg(ParkingSpace allocatedParkingSpace) {
        allocatedParkingSpace.setVehicleReg(null);
        allocatedParkingSpace.setVehicleType(null);
        allocatedParkingSpace.setTimeIn(null);

        try {
            parkingSpaceRepository.save(allocatedParkingSpace);

            if (allocatedParkingSpace.getId() == null) {
                throw new DatabaseErrorException("Unable to save deallocated parking space", "err-db3", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            updateParkingSpaceInventory(false);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Unable to save deallocated parking space in the database", "err-db1" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
