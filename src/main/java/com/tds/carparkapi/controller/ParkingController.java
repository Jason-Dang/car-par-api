package com.tds.carparkapi.controller;

import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.service.ParkingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ParkingController
{
    @Autowired
    private ParkingService parkingService;

    @GetMapping("/api/parking")
    public ResponseEntity<ParkingSpacesInventoryDTO> getParkingSpacesInventory() {
        return ResponseEntity.ok(parkingService.getParkingSpacesInventory());
    }

    @PostMapping("/api/parking")
    public ResponseEntity<Object> getNextAvailableParkingSpace(@RequestBody Map<String, Object> requestData) {
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

        ParkingSpace alreadyAllocatedParkingSpace = parkingService.getAllocatedParkingSpaceForVehicleReg(vehicleReg);

        if (alreadyAllocatedParkingSpace != null) {
            return ResponseEntity.badRequest().body("Vehicle registration already parked in space");
        }

        if (!requestData.containsKey("vehicleType")) {
            return ResponseEntity.badRequest().body("Vehicle type must be provided");
        }

        if (requestData.get("vehicleType") == null) {
            return ResponseEntity.badRequest().body("Vehicle type cannot be null");
        }

        Integer vehicleType = Integer.parseInt(requestData.get("vehicleType").toString());

        return ResponseEntity.ok(parkingService.getNextAvailableParkingSpace(vehicleReg, vehicleType));
    }

    @PostMapping("/api/parking/bill")
    public ResponseEntity<Object> getBillForVehicleReg(@RequestBody Map<String, Object> requestData) {
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

        ParkingSpace allocatedParkingSpace = parkingService.getAllocatedParkingSpaceForVehicleReg(vehicleReg);

        if (allocatedParkingSpace == null) {
            return ResponseEntity.badRequest().body("Vehicle registration not found in any parking spaces");
        }

        return ResponseEntity.ok(parkingService.getParkingBillForVehicleReg(allocatedParkingSpace));
    }
}
