package com.jd.carparkapi.service;

import com.jd.carparkapi.dto.ParkResponse;
import com.jd.carparkapi.dto.ParkingSpacesResponse;
import com.jd.carparkapi.dto.ParkingSpaceSummaryResponse;
import com.jd.carparkapi.dto.ParkingSpaceSummaryItemResponse;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseConnectionException;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseErrorException;
import com.jd.carparkapi.exceptionhandling.customexceptions.ResourceNotFoundException;
import com.jd.carparkapi.mapper.ParkingMapper;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    private final ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    public ParkingSpaceService(
        ParkingSpaceRepository parkingSpaceRepository,
        ParkingSpaceInventoryRepository parkingSpaceInventoryRepository
    ) {
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceInventoryRepository = parkingSpaceInventoryRepository;
    }

    public void updateParkingSpaceInventory(boolean allocatedParkingSpace) {
        ParkingSpaceInventory parkingSpaceInventory = parkingSpaceInventoryRepository.findOneById(1L);

        if (parkingSpaceInventory == null) {
            throw new ResourceNotFoundException(
                "No parking space inventory found",
                "err-ps1",
                HttpStatus.NOT_FOUND
            );
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
        } catch (Exception _) {
            throw new DatabaseConnectionException(
                "Unable to update parking space inventory in the database",
                "err-db1",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ParkingSpacesResponse getParkingSpaceInventory() {
        ParkingSpaceInventory parkingSpaceInventory = parkingSpaceInventoryRepository.findOneById(1L);

        if (parkingSpaceInventory == null) {
            throw new ResourceNotFoundException(
                "No parking space inventory found",
                "err-ps1",
                HttpStatus.NOT_FOUND
            );
        }

        return ParkingMapper.mapToParkingSpacesResponse(parkingSpaceInventory);
    }

    public ParkResponse allocateNextAvailableParkingSpace(String vehicleReg, Integer vehicleType) {
        ParkingSpace availableParkingSpace = parkingSpaceRepository.findNextAvailableParkingSpace();

        if (availableParkingSpace == null || availableParkingSpace.getId() == null) {
            throw new ResourceNotFoundException(
                "No available parking space found",
                "err-ps3",
                HttpStatus.NOT_FOUND
            );
        }

        availableParkingSpace.setVehicleReg(vehicleReg);
        availableParkingSpace.setVehicleType(vehicleType);
        availableParkingSpace.setTimeIn(LocalDateTime.now());

        ParkingSpace allocatedParkingSpace;
        try {
            allocatedParkingSpace = parkingSpaceRepository.save(availableParkingSpace);
        } catch (Exception _) {
            throw new DatabaseConnectionException(
                "Unable to save allocated parking space in the database",
                "err-db1",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        if (allocatedParkingSpace.getId() == null) {
            throw new DatabaseErrorException(
                "Unable to save allocated parking space",
                "err-db2",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        updateParkingSpaceInventory(true);

        return ParkingMapper.mapToParkingResponse(allocatedParkingSpace);
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
        } catch (Exception _) {
            throw new DatabaseConnectionException(
                "Unable to save deallocated parking space in the database",
                "err-db1",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        updateParkingSpaceInventory(false);
    }

    public ParkingSpaceSummaryResponse getParkingSpaceSummary() {
        List<ParkingSpace> parkingSpaces = parkingSpaceRepository.findAll();

        if (parkingSpaces.isEmpty()) {
            return new ParkingSpaceSummaryResponse(List.of());
        }

        List<ParkingSpaceSummaryItemResponse> summaryList = new ArrayList<>();

        for (ParkingSpace parkingSpace : parkingSpaces) {
            if (parkingSpace.getTimeIn() == null) {
                continue;
            }

            Duration diff = Duration.between(parkingSpace.getTimeIn(), LocalDateTime.now());
            BigDecimal minutesStayed = BigDecimal.valueOf(diff.toMinutes());

            summaryList.add(new ParkingSpaceSummaryItemResponse(
                parkingSpace.getVehicleReg(),
                minutesStayed.intValueExact()
            ));
        }

        return new ParkingSpaceSummaryResponse(summaryList);
    }
}
