package com.tds.carparkapi.Service;

import com.tds.carparkapi.DTO.ParkingBillDTO;
import com.tds.carparkapi.Entity.ParkingBill;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.InvalidDataException;
import com.tds.carparkapi.Respository.ParkingBillRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

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

    @Test
    void get1MinuteParkingBillForVehicleType1() {
        Integer vehicleType = 1;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(1);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillDTO parkingBillDTO = parkingBillService.getParkingBill(
                vehicleReg,
                vehicleType,
                timeIn,
                timeOut
        );

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.1, parkingBillDTO.getVehicleCharge());
    }

    @Test
    void get1MinuteParkingBillForVehicleType2() {
        Integer vehicleType = 2;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(1);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillDTO parkingBillDTO = parkingBillService.getParkingBill(
                vehicleReg,
                vehicleType,
                timeIn,
                timeOut
        );

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.2, parkingBillDTO.getVehicleCharge());
    }

    @Test
    void get1MinuteParkingBillForVehicleType3() {
        Integer vehicleType = 3;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(1);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillDTO parkingBillDTO = parkingBillService.getParkingBill(
                vehicleReg,
                vehicleType,
                timeIn,
                timeOut
        );

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.4, parkingBillDTO.getVehicleCharge());
    }

    @Test
    void get1MinuteParkingBillForInvalidVehicleType() {
        Integer vehicleType = 0;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(1);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        InvalidDataException exception = Assertions.assertThrows(InvalidDataException.class, () -> parkingBillService.getParkingBill(
            vehicleReg,
            vehicleType,
            timeIn,
            timeOut
        ));

        Assertions.assertEquals("Vehicle type must be either: '1', '2' or '3'", exception.getMessage());
    }
}
