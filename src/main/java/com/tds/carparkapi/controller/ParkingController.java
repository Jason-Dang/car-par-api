package com.tds.carparkapi.controller;

import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
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
        String vehicleReg = requestData.get("vehicleReg").toString();
        Integer vehicleType = Integer.parseInt(requestData.get("vehicleType").toString());

        if (!requestData.containsKey("vehicleReg")) {
            return ResponseEntity.badRequest().body("Must provide vehicleReg");
        }

        if (!requestData.containsKey("vehicleType")) {
            return ResponseEntity.badRequest().body("Must provide vehicleType");
        }

        return ResponseEntity.ok(parkingService.getNextAvailableParkingSpace(vehicleReg, vehicleType));
    }

    @PostMapping("/api/parking/bill")
    public ResponseEntity<Object> getBillForVehicleReg(@RequestBody Map<String, Object> requestData) {
        String vehicleReg = requestData.get("vehicleReg").toString();

        if (!requestData.containsKey("vehicleReg")) {
            return ResponseEntity.badRequest().body("Must provide vehicleReg");
        }

        return ResponseEntity.ok(parkingService.getParkingBillForVehicleReg(vehicleReg));
    }
}
