package com.tds.carparkapi.service;

import com.tds.carparkapi.respository.ParkingBillRepository;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceUnitTest {
    @Mock
    private ParkingBillRepository parkingBillRepository;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingService parkingService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        parkingService = new ParkingService(
                parkingBillRepository,
                parkingSpaceRepository,
                parkingSpaceInventoryRepository
        );
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingBillRepository);
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }
}
