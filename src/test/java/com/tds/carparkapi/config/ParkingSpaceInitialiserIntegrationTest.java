package com.tds.carparkapi.config;

import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.entity.ParkingSpaceInventory;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.List;

@SpringBootTest
@TestConfiguration
public class ParkingSpaceInitialiserIntegrationTest {
    @Value("20")
    @Inject
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
        assert parkingSpaceInventory != null;

        assert parkingSpaceInventory.getAvailableSpaces() != null;
        Assertions.assertThat(parkingSpaceInventory.getAvailableSpaces().equals(totalSpaces));

        assert parkingSpaceInventory.getOccupiedSpaces() != null;
        Assertions.assertThat(parkingSpaceInventory.getOccupiedSpaces().equals(0));
    }

    @Test
    void initialisedAvailableParkingSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseAvailableParkingSpaces();

        List<ParkingSpace> parkingSpaceList = parkingSpaceRepository.findAll();
        Assertions.assertThat(parkingSpaceList).isNotEmpty();

        ParkingSpace firstSpace = parkingSpaceList.getFirst();
        Assertions.assertThat(firstSpace.getId().equals(1L));
        assert firstSpace.getVehicleReg() == null;
        assert firstSpace.getVehicleType() == null;
        assert firstSpace.getTimeIn() == null;

        ParkingSpace lastSpace = parkingSpaceList.getLast();
        Assertions.assertThat(lastSpace.getId().equals(Long.valueOf(totalSpaces)));
        assert lastSpace.getVehicleReg() == null;
        assert lastSpace.getVehicleType() == null;
        assert lastSpace.getTimeIn() == null;
    }
}
