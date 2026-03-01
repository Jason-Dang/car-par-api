package com.tds.carparkapi.controller;

import com.tds.carparkapi.model.dto.ParkingSpaceInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.service.ParkingBillService;
import com.tds.carparkapi.service.ParkingSpaceService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Object> getNextAvailableParkingSpace(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey("vehicleReg")) {
            return ResponseEntity.badRequest().body("Vehicle registration must be provided");
        }

        if (!requestData.containsKey("vehicleType")) {
            return ResponseEntity.badRequest().body("Vehicle type must be provided");
        }

        if (requestData.get("vehicleReg") == null) {
            return ResponseEntity.badRequest().body("Vehicle registration cannot be null");
        }

        if (requestData.get("vehicleType") == null) {
            return ResponseEntity.badRequest().body("Vehicle type cannot be null");
        }

        Integer vehicleType = Integer.parseInt(requestData.get("vehicleType").toString());
        String vehicleReg = requestData.get("vehicleReg").toString();

        if (vehicleReg.isEmpty()) {
            return ResponseEntity.badRequest().body("Vehicle registration cannot be empty");
        }

        if (parkingSpaceService.getAllocatedParkingSpace(vehicleReg) != null) {
            return ResponseEntity.badRequest().body("Vehicle registration already parked in space");
        }

        return ResponseEntity.ok(parkingSpaceService.allocateNextAvailableParkingSpace(vehicleReg, vehicleType));
    }

    @PostMapping("/api/parking/bill")
    public ResponseEntity<Object> getParkingBill(@RequestBody Map<String, Object> requestData) {
        if (!requestData.containsKey("vehicleReg")) {
            return ResponseEntity.badRequest().body("Vehicle registration must be provided");
        }

        if (requestData.get("vehicleReg") == null) {
            return ResponseEntity.badRequest().body("Vehicle registration cannot be null");
        }

        String vehicleReg = requestData.get("vehicleReg").toString();

        if (vehicleReg.isEmpty()) {
            return ResponseEntity.badRequest().body("Vehicle registration cannot be empty");
        }

        ParkingSpace allocatedParkingSpace = parkingSpaceService.getAllocatedParkingSpace(vehicleReg);

        if (allocatedParkingSpace == null) {
            return ResponseEntity.badRequest().body("Vehicle registration not found in any parking spaces");
        }

        LocalDateTime timeOut = LocalDateTime.now();
        Integer vehicleType = allocatedParkingSpace.getVehicleType();
        LocalDateTime timeIn = allocatedParkingSpace.getTimeIn();

        parkingSpaceService.deallocateParkingSpaceForReg(allocatedParkingSpace);

        return ResponseEntity.ok(parkingBillService.getParkingBill(
            timeOut,
            vehicleReg,
            vehicleType,
            timeIn
        ));
    }
}
