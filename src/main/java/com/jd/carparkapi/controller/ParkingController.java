package com.jd.carparkapi.controller;

import com.jd.carparkapi.dto.OccupiedParkingSpaceDTO;
import com.jd.carparkapi.dto.ParkingBillDTO;
import com.jd.carparkapi.dto.ParkingSpaceInventoryDTO;
import com.jd.carparkapi.dto.ParkingSpaceSummaryDTO;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.service.ParkingBillService;
import com.jd.carparkapi.service.ParkingSpaceService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ParkingController
{
    private static final String VEHICLE_REG = "vehicleReg";
    private static final String VEHICLE_TYPE = "vehicleType";

    private final ParkingSpaceService parkingSpaceService;

    private final ParkingBillService parkingBillService;

    public ParkingController(ParkingSpaceService parkingSpaceService, ParkingBillService parkingBillService) {
        this.parkingSpaceService = parkingSpaceService;
        this.parkingBillService = parkingBillService;
    }

    @GetMapping("/parking")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSpaceInventoryDTO> getParkingSpaceInventory() {
        return ResponseEntity.ok(parkingSpaceService.getParkingSpaceInventory());
    }

    @PostMapping("/parking")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OccupiedParkingSpaceDTO> getNextAvailableParkingSpace(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey(VEHICLE_REG)) {
            throw new InvalidDataException(
                "Vehicle registration must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (!requestData.containsKey(VEHICLE_TYPE)) {
            throw new InvalidDataException(
                "Vehicle type must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get(VEHICLE_REG) == null) {
            throw new InvalidDataException(
                "Vehicle registration cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get(VEHICLE_TYPE) == null) {
            throw new InvalidDataException(
                "Vehicle type cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        Integer vehicleType = Integer.parseInt(requestData.get(VEHICLE_TYPE).toString());
        String vehicleReg = requestData.get(VEHICLE_REG).toString();

        if (vehicleReg.isEmpty()) {
            throw new InvalidDataException(
                "Vehicle registration cannot be empty",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (parkingSpaceService.getAllocatedParkingSpace(vehicleReg) != null) {
            throw new InvalidDataException(
                "Vehicle registration already parked in space",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        return ResponseEntity.ok(parkingSpaceService.allocateNextAvailableParkingSpace(vehicleReg, vehicleType));
    }

    @PostMapping("/parking/bill")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingBillDTO> getParkingBill(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey(VEHICLE_REG)) {
            throw new InvalidDataException(
                "Vehicle registration must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get(VEHICLE_REG) == null) {
            throw new InvalidDataException(
                "Vehicle registration cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        String vehicleReg = requestData.get(VEHICLE_REG).toString();

        if (vehicleReg.isEmpty()) {
            throw new InvalidDataException(
                "Vehicle registration cannot be empty",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        ParkingSpace allocatedParkingSpace = parkingSpaceService.getAllocatedParkingSpace(vehicleReg);

        if (allocatedParkingSpace == null) {
            throw new InvalidDataException(
                "Vehicle registration not found in any parking spaces",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        LocalDateTime timeOut = LocalDateTime.now();
        Integer vehicleType = allocatedParkingSpace.getVehicleType();
        LocalDateTime timeIn = allocatedParkingSpace.getTimeIn();

        parkingSpaceService.deallocateParkingSpaceForReg(allocatedParkingSpace);

        return ResponseEntity.ok(parkingBillService.getParkingBill(
                vehicleReg,
                vehicleType,
                timeIn,
                timeOut
        ));
    }

    @GetMapping("/parking/summary")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingSpaceSummaryDTO> getParkingSpaceSummary() {
        return ResponseEntity.ok(parkingSpaceService.getParkingSpaceSummary());
    }
}
