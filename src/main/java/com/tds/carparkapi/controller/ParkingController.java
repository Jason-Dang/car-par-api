package com.tds.carparkapi.controller;

import com.tds.carparkapi.model.dto.ParkingSpaceInventoryDTO;
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

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/api/parking")
    public ResponseEntity<ParkingSpaceInventoryDTO> getParkingSpaceInventory() {
        return ResponseEntity.ok(parkingService.getParkingSpaceInventory());
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

        if (parkingService.getAllocatedParkingSpaceForVehicleReg(vehicleReg) != null) {
            return ResponseEntity.badRequest().body("Vehicle registration already parked in space");
        }

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
