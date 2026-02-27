package com.tds.carparkapi.controller;

import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.service.ParkingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ParkingController
{
    @Autowired
    private ParkingService parkingService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/api/parking")
    public ResponseEntity<ParkingSpacesInventoryDTO> getParkingSpacesInventory(
            @Value("${app.config.totalSpaces}") Integer totalSpaces,
            @SessionAttribute(name = "availableSpaces", required = false) Integer availableSpaces,
            @SessionAttribute(name = "occupiedSpaces", required = false) Integer occupiedSpaces
    ) {
        Map<String, Integer> sessionValues = parkingService.initialiseParkingSpacesInventory(request, totalSpaces);
        return ResponseEntity.ok(parkingService.getParkingSpacesInventory(
            sessionValues.get("availableSpaces"), sessionValues.get("occupiedSpaces")
        ));
    }

    @PostMapping("/api/parking")
    public ResponseEntity<Object> getNextAvailableParkingSpace(
            @Value("${app.config.totalSpaces}") Integer totalSpaces,
            @SessionAttribute(name = "availableSpaces", required = false) Integer availableSpaces,
            @SessionAttribute(name = "occupiedSpaces", required = false) Integer occupiedSpaces,
            @RequestBody Map<String, Object> requestData
    ) {
        Map<String, Integer> sessionValues = parkingService.initialiseParkingSpacesInventory(request, totalSpaces);

        String vehicleReg = requestData.get("vehicleReg").toString();
        Integer vehicleType = Integer.parseInt(requestData.get("vehicleType").toString());

        if (!requestData.containsKey("vehicleReg")) {
            return ResponseEntity.badRequest().body("Must provide vehicleReg");
        }

        if (!requestData.containsKey("vehicleType")) {
            return ResponseEntity.badRequest().body("Must provide vehicleType");
        }

        return ResponseEntity.ok(parkingService.getNextAvailableParkingSpace(
            vehicleReg, vehicleType, request)
        );
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
