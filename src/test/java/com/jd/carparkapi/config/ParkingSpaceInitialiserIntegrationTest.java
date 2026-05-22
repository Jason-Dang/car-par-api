package com.jd.carparkapi.config;

import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.List;

@SpringBootTest
@TestConfiguration
class ParkingSpaceInitialiserIntegrationTest {
    @Value("20")
    private Integer totalSpaces;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @Autowired
    private ParkingSpaceRepositoryInitialiser parkingSpaceRepositoryInitialiser;

    @Test
    void initialisedParkingSpaceInventory() {
        parkingSpaceRepositoryInitialiser.initialiseParkingSpaceInventory();

        ParkingSpaceInventory parkingSpaceInventory = parkingSpaceInventoryRepository.findOneById(1L);
        Assertions.assertNotNull(parkingSpaceInventory);

        Assertions.assertNotNull(parkingSpaceInventory.getAvailableSpaces());
        Assertions.assertEquals(totalSpaces, parkingSpaceInventory.getAvailableSpaces());

        Assertions.assertNotNull(parkingSpaceInventory.getOccupiedSpaces());
        Assertions.assertEquals(0, parkingSpaceInventory.getOccupiedSpaces());
    }

    @Test
    void initialisedAvailableParkingSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseAvailableParkingSpaces();

        List<ParkingSpace> parkingSpaceList = parkingSpaceRepository.findAll();
        Assertions.assertFalse(parkingSpaceList.isEmpty());

        ParkingSpace firstSpace = parkingSpaceList.getFirst();
        Assertions.assertEquals(1L, firstSpace.getId());
        Assertions.assertNull(firstSpace.getVehicleReg());
        Assertions.assertNull(firstSpace.getVehicleType());
        Assertions.assertNull(firstSpace.getTimeIn());

        ParkingSpace lastSpace = parkingSpaceList.getLast();
        Assertions.assertEquals(Long.valueOf(totalSpaces), lastSpace.getId());
        Assertions.assertNull(lastSpace.getVehicleReg());
        Assertions.assertNull(lastSpace.getVehicleType());
        Assertions.assertNull(lastSpace.getTimeIn());
    }
}
