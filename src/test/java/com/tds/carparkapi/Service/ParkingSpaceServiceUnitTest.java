package com.tds.carparkapi.Service;

import com.tds.carparkapi.Respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.Respository.ParkingSpaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParkingSpaceServiceUnitTest {

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingSpaceService parkingSpaceService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        parkingSpaceService = new ParkingSpaceService(parkingSpaceRepository, parkingSpaceInventoryRepository);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }
}
