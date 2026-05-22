package com.jd.carparkapi.config;

import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceInitialiserUnitTest {
    private final Integer totalSpaces = 20;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    private ParkingSpaceRepositoryInitialiser parkingSpaceRepositoryInitialiser;

    @BeforeEach
    void setup() {
        parkingSpaceRepositoryInitialiser = new ParkingSpaceRepositoryInitialiser(
            totalSpaces,
            parkingSpaceRepository,
            parkingSpaceInventoryRepository
        );
    }

    @AfterEach
    void reset() {
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }

    @Test
    void initialisedParkingSpaceInventoryWithTotalSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseParkingSpaceInventory();

        ArgumentCaptor<ParkingSpaceInventory> parkingSpaceInventoryCaptor = ArgumentCaptor.forClass(ParkingSpaceInventory.class);
        verify(parkingSpaceInventoryRepository, times(1)).save(parkingSpaceInventoryCaptor.capture());
        Assertions.assertEquals(totalSpaces, parkingSpaceInventoryCaptor.getValue().getAvailableSpaces());
        Assertions.assertEquals(0, parkingSpaceInventoryCaptor.getValue().getOccupiedSpaces());
    }

    @Test
    void initialisedAvailableParkingSpacesWithTotalSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseAvailableParkingSpaces();

        ArgumentCaptor<ParkingSpace> parkingSpaceCaptor = ArgumentCaptor.forClass(ParkingSpace.class);
        verify(parkingSpaceRepository, times(totalSpaces)).save(parkingSpaceCaptor.capture());

        List<ParkingSpace> captives = parkingSpaceCaptor.getAllValues();
        Assertions.assertEquals(totalSpaces, captives.size());
    }

    @Test
    void doesNotInitialiseWhenTotalSpacesIsZero() {
        ParkingSpaceRepositoryInitialiser zeroSpaceInitialiser = new ParkingSpaceRepositoryInitialiser(
            0,
            parkingSpaceRepository,
            parkingSpaceInventoryRepository
        );

        zeroSpaceInitialiser.initialiseParkingSpaceInventory();
        zeroSpaceInitialiser.initialiseAvailableParkingSpaces();

        verify(parkingSpaceInventoryRepository, times(0)).save(Mockito.any());
        verify(parkingSpaceRepository, times(0)).save(Mockito.any());
    }
}