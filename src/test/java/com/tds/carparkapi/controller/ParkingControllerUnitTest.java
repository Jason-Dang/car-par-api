package com.tds.carparkapi.controller;


import com.tds.carparkapi.service.ParkingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParkingControllerUnitTest
{
    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        parkingController = new ParkingController(parkingService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingService);
    }
}
