package com.tds.carparkapi.service;

import com.tds.carparkapi.respository.ParkingBillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParkingBillServiceUnitTest {

    @Mock
    private ParkingBillRepository parkingBillRepository;

    @InjectMocks
    private ParkingBillService parkingBillService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        parkingBillService = new ParkingBillService(parkingBillRepository);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingBillRepository);
    }
}
