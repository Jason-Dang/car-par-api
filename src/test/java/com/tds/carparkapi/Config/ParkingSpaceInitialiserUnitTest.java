package com.tds.carparkapi.Config;

import com.tds.carparkapi.Entity.ParkingSpace;
import com.tds.carparkapi.Entity.ParkingSpaceInventory;
import com.tds.carparkapi.Respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.Respository.ParkingSpaceRepository;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParkingSpaceInitialiserUnitTest {
    @Inject
    private Integer totalSpaces = 20;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingSpaceRepositoryInitialiser parkingSpaceRepositoryInitialiser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        parkingSpaceRepositoryInitialiser = new ParkingSpaceRepositoryInitialiser(
                totalSpaces,
                parkingSpaceRepository,
                parkingSpaceInventoryRepository
        );
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }

    @Test
    void initialisedParkingSpaceInventoryWithTotalSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseParkingSpaceInventory();

        ArgumentCaptor<ParkingSpaceInventory> parkingSpaceInventoryCaptor = ArgumentCaptor.forClass(ParkingSpaceInventory.class);
        verify(parkingSpaceInventoryRepository, times(1)).save(parkingSpaceInventoryCaptor.capture());
        Assertions.assertThat(parkingSpaceInventoryCaptor.getValue().getAvailableSpaces().equals(totalSpaces));
        Assertions.assertThat(parkingSpaceInventoryCaptor.getValue().getOccupiedSpaces().equals(0));
    }

    @Test
    void initialisedAvailableParkingSpacesWithTotalSpaces() {
        parkingSpaceRepositoryInitialiser.initialiseAvailableParkingSpaces();

        ArgumentCaptor<ParkingSpace> parkingSpaceCaptor = ArgumentCaptor.forClass(ParkingSpace.class);
        verify(parkingSpaceRepository, times(totalSpaces)).save(parkingSpaceCaptor.capture());

        List<ParkingSpace> captives = parkingSpaceCaptor.getAllValues();
        Assertions.assertThat(captives.size() == totalSpaces);

        ParkingSpace firstSpace = captives.getFirst();
        assert firstSpace != null;

        ParkingSpace lastSpace = captives.getLast();
        assert lastSpace != null;
    }
}
