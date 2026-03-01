package com.tds.carparkapi.Controller;


import com.tds.carparkapi.Service.ParkingSpaceService;
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
    private ParkingSpaceService parkingSpaceService;

    @InjectMocks
    private ParkingController parkingController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        parkingController = new ParkingController(parkingSpaceService);
    }

    @AfterEach
    public void reset() {
        Mockito.reset(parkingSpaceService);
    }
}
