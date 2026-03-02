package com.tds.carparkapi.Controller;

import com.tds.carparkapi.DTO.OccupiedParkingSpaceDTO;
import com.tds.carparkapi.DTO.ParkingBillDTO;
import com.tds.carparkapi.DTO.ParkingSpaceInventoryDTO;
import com.tds.carparkapi.Entity.ParkingSpace;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.InvalidDataException;
import com.tds.carparkapi.Service.ParkingBillService;
import com.tds.carparkapi.Service.ParkingSpaceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class ParkingController
{
    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private ParkingBillService parkingBillService;

    public ParkingController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("/api/parking")
    public ResponseEntity<ParkingSpaceInventoryDTO> getParkingSpaceInventory() {
        return ResponseEntity.ok(parkingSpaceService.getParkingSpaceInventory());
    }

    @PostMapping("/api/parking")
    public ResponseEntity<OccupiedParkingSpaceDTO> getNextAvailableParkingSpace(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey("vehicleReg")) {
            throw new InvalidDataException(
                "Vehicle registration must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (!requestData.containsKey("vehicleType")) {
            throw new InvalidDataException(
                "Vehicle type must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get("vehicleReg") == null) {
            throw new InvalidDataException(
                "Vehicle registration cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get("vehicleType") == null) {
            throw new InvalidDataException(
                "Vehicle type cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        Integer vehicleType = Integer.parseInt(requestData.get("vehicleType").toString());
        String vehicleReg = requestData.get("vehicleReg").toString();

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

    @PostMapping("/api/parking/bill")
    public ResponseEntity<ParkingBillDTO> getParkingBill(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey("vehicleReg")) {
            throw new InvalidDataException(
                "Vehicle registration must be provided",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        if (requestData.get("vehicleReg") == null) {
            throw new InvalidDataException(
                "Vehicle registration cannot be null",
                "err-ps0",
                HttpStatus.BAD_REQUEST
            );
        }

        String vehicleReg = requestData.get("vehicleReg").toString();

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
}
