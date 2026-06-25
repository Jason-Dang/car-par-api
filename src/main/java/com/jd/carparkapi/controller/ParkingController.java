package com.jd.carparkapi.controller;

import com.jd.carparkapi.dto.*;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.service.ParkingBillService;
import com.jd.carparkapi.service.ParkingSpaceService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    public ResponseEntity<ParkingSpacesResponse> getParkingSpaceInventory() {
        return ResponseEntity.ok(parkingSpaceService.getParkingSpaceInventory());
    }

    @PostMapping("/parking")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkResponse> getNextAvailableParkingSpace(
        @Valid
        @RequestBody
        ParkRequest requestData
    ) {
        Integer vehicleType = requestData.vehicleType();
        String vehicleReg = requestData.vehicleReg();

        if (parkingSpaceService.getAllocatedParkingSpace(vehicleReg) != null) {
            throw new InvalidDataException(
                "Vehicle registration already parked in space",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        return ResponseEntity.ok(parkingSpaceService.allocateNextAvailableParkingSpace(
            vehicleReg,
            vehicleType,
            null,
            null
        ));
    }

    @PostMapping("/parking/bill")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingBillResponse> getParkingBill(
        @Valid
        @RequestBody
        ParkingBillRequest requestData
    ) {
        if (requestData.vehicleReg() == null || requestData.vehicleReg().isEmpty()) {
            throw new InvalidDataException(
                "Vehicle registration must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        String vehicleReg = requestData.vehicleReg();

        ParkingSpace allocatedParkingSpace = parkingSpaceService.getAllocatedParkingSpace(vehicleReg);

        if (allocatedParkingSpace == null) {
            throw new InvalidDataException(
                "Vehicle registration not found in any parking spaces",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        LocalDateTime timeOut = LocalDateTime.now(ZoneOffset.UTC);
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

    @GetMapping("/admin/parking/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceSummaryResponse> getParkingSpaceSummary() {
        return ResponseEntity.ok(parkingSpaceService.getParkingSpaceSummary());
    }


    // Book in advance for specified period
    // Time + Date In
    // Expected Time + Date Out
    // Charge at premium rate for overstay
    // Endpoint for checking out (handle if availability changes within interim)

    @PostMapping("/parkingperiod")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ParkingBillResponse> bookParkingPeriod(
        @Valid
        @RequestBody
        ParkPeriodRequest requestData
    ) {
        Integer vehicleType = requestData.vehicleType();
        String vehicleReg = requestData.vehicleReg();
        LocalDateTime timeIn = requestData.timeIn();
        LocalDateTime timeOut = requestData.timeOut();

        if (parkingSpaceService.getAllocatedParkingSpace(vehicleReg) != null) {
            throw new InvalidDataException(
                "Vehicle registration already parked in space",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        parkingSpaceService.allocateNextAvailableParkingSpace(
            vehicleReg,
            vehicleType,
            timeIn,
            timeOut
        );

        return ResponseEntity.ok(parkingBillService.getParkingBill(
            vehicleReg,
            vehicleType,
            timeIn,
            timeOut
        ));
    }

}
