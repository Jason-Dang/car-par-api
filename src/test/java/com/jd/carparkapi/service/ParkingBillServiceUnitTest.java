package com.jd.carparkapi.service;

import com.jd.carparkapi.dto.ParkingBillResponse;
import com.jd.carparkapi.entity.ParkingBill;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.respository.ParkingBillRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingBillServiceUnitTest {

    @Mock
    private ParkingBillRepository parkingBillRepository;

    @InjectMocks
    private ParkingBillService parkingBillService;

    @AfterEach
    void reset() {
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

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.1, parkingBillDTO.vehicleCharge());
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

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.2, parkingBillDTO.vehicleCharge());
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

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(.4, parkingBillDTO.vehicleCharge());
    }

    @Test
    void get1MinuteParkingBillForInvalidVehicleType() {
        Integer vehicleType = 0;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(1);

        InvalidDataException exception = Assertions.assertThrows(InvalidDataException.class, () ->
            parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut)
        );

        Assertions.assertEquals("Vehicle type must be either: '1', '2' or '3'", exception.getMessage());
    }

    @Test
    void get5MinuteParkingBillForVehicleType1() {
        Integer vehicleType = 1;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(5);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(1.5, parkingBillDTO.vehicleCharge());
    }

    @Test
    void get10MinuteParkingBillForVehicleType1() {
        Integer vehicleType = 1;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(10);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(3.0, parkingBillDTO.vehicleCharge());
    }

    @Test
    void get15MinuteParkingBillForVehicleType1() {
        Integer vehicleType = 1;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(15);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(4.5, parkingBillDTO.vehicleCharge());
    }

    @Test
    void get30MinuteParkingBillForVehicleType1() {
        Integer vehicleType = 1;
        String vehicleReg = "ABC 123";
        LocalDateTime timeIn = LocalDateTime.now();
        LocalDateTime timeOut = LocalDateTime.now().plusMinutes(30);

        when(parkingBillRepository.save(Mockito.any(ParkingBill.class))).thenAnswer(
            invocation -> invocation.getArguments()[0]
        );

        ParkingBillResponse parkingBillDTO = parkingBillService.getParkingBill(vehicleReg, vehicleType, timeIn, timeOut);

        Assertions.assertNotNull(parkingBillDTO);
        Assertions.assertEquals(9.0, parkingBillDTO.vehicleCharge());
    }
}