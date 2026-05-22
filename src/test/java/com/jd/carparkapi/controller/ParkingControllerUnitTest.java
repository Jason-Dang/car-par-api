package com.jd.carparkapi.controller;


import com.jd.carparkapi.service.ParkingBillService;
import com.jd.carparkapi.service.ParkingSpaceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class ParkingControllerUnitTest
{
    @Mock
    private ParkingSpaceService parkingSpaceService;

    @Mock
    private ParkingBillService parkingBillService;

    @InjectMocks
    private ParkingController parkingController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        this.parkingController = new ParkingController(parkingSpaceService, parkingBillService);
    }

    @AfterEach
    void reset() {
        Mockito.reset(parkingSpaceService);
        Mockito.reset(parkingBillService);
    }
}
