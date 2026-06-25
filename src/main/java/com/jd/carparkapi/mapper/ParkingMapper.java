package com.jd.carparkapi.mapper;

import com.jd.carparkapi.dto.ParkResponse;
import com.jd.carparkapi.dto.ParkingSpacesResponse;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;

public class ParkingMapper {
    public static ParkingSpacesResponse mapToParkingSpacesResponse(ParkingSpaceInventory parkingSpaceInventory) {
        return new ParkingSpacesResponse(
            parkingSpaceInventory.getAvailableSpaces(),
            parkingSpaceInventory.getOccupiedSpaces()
        );
    }

    public static ParkResponse mapToParkingResponse(ParkingSpace parkingSpace) {
        return new ParkResponse(
            parkingSpace.getId(),
            parkingSpace.getVehicleReg(),
            parkingSpace.getTimeIn()
        );
    }
}
