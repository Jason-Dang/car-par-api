package com.jd.carparkapi.config;

import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
class ParkingSpaceInitialiserUnitTest {
    @Inject
    private Integer totalSpaces = 20;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingSpaceRepositoryInitialiser parkingSpaceRepositoryInitialiser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

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

        ParkingSpace firstSpace = captives.getFirst();
        Assertions.assertNotNull(firstSpace);

        ParkingSpace lastSpace = captives.getLast();
        Assertions.assertNotNull(lastSpace);
    }
}
