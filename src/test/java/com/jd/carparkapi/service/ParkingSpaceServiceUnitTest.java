package com.jd.carparkapi.service;

import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class ParkingSpaceServiceUnitTest {

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingSpaceService parkingSpaceService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        parkingSpaceService = new ParkingSpaceService(parkingSpaceRepository, parkingSpaceInventoryRepository);
    }

    @AfterEach
    void reset() {
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }
}
