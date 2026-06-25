package com.jd.carparkapi.controller;

import com.jd.carparkapi.dto.*;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.service.ParkingBillService;
import com.jd.carparkapi.service.ParkingSpaceService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingControllerUnitTest {

    @Mock
    private ParkingSpaceService parkingSpaceService;

    @Mock
    private ParkingBillService parkingBillService;

    @InjectMocks
    private ParkingController parkingController;

    @AfterEach
    void reset() {
        Mockito.reset(parkingSpaceService);
        Mockito.reset(parkingBillService);
    }

    // --- GET /parking ---

    @Test
    void getParkingSpaceInventory_returns200WithInventory() {
        ParkingSpacesResponse inventory = new ParkingSpacesResponse(17, 3);
        when(parkingSpaceService.getParkingSpaceInventory()).thenReturn(inventory);

        ResponseEntity<ParkingSpacesResponse> response = parkingController.getParkingSpaceInventory();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(inventory, response.getBody());
    }

    // --- POST /parking ---

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegKeyMissing() {
        ParkRequest requestData = new ParkRequest(null, 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleTypeKeyMissing() {
        ParkRequest requestData = new ParkRequest("ABC123", null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegIsNull() {
        ParkRequest requestData = new ParkRequest(null, 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleTypeIsNull() {
        ParkRequest requestData = new ParkRequest("ABC123", null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegIsEmpty() {
        ParkRequest requestData = new ParkRequest("", 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleAlreadyParked() {
        String vehicleReg = "ABC123";
        ParkRequest requestData = new ParkRequest(vehicleReg, 1);

        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(mock(ParkingSpace.class));

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_returns200WithOccupiedSpaceDTO() {
        String vehicleReg = "ABC123";
        int vehicleType = 1;
        ParkRequest requestData = new ParkRequest(vehicleReg, vehicleType);

        ParkResponse occupiedDTO = new ParkResponse(5L, vehicleReg, LocalDateTime.now(ZoneOffset.UTC));
        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(null);
        when(parkingSpaceService.allocateNextAvailableParkingSpace(
            vehicleReg,
            vehicleType,
            null,
            null
        )).thenReturn(occupiedDTO);

        ResponseEntity<ParkResponse> response = parkingController.getNextAvailableParkingSpace(requestData);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(occupiedDTO, response.getBody());
    }

    // --- POST /parking/bill ---

    @Test
    void getParkingBill_throwsWhenVehicleRegIsNull() {
        ParkingBillRequest requestData = new ParkingBillRequest(null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_throwsWhenVehicleRegIsEmpty() {
        ParkingBillRequest requestData = new ParkingBillRequest("");

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_throwsWhenVehicleNotParked() {
        String vehicleReg = "ABC123";
        ParkingBillRequest requestData = new ParkingBillRequest(vehicleReg);

        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_returns200WithBillDTO() {
        String vehicleReg = "ABC123";
        ParkingBillRequest requestData = new ParkingBillRequest(vehicleReg);

        LocalDateTime timeIn = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(30);
        ParkingSpace space = mock(ParkingSpace.class);
        when(space.getVehicleType()).thenReturn(1);
        when(space.getTimeIn()).thenReturn(timeIn);

        ParkingBillResponse billDTO = new ParkingBillResponse(1L, vehicleReg, 9.0, timeIn, LocalDateTime.now(ZoneOffset.UTC));
        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(space);
        when(parkingBillService.getParkingBill(eq(vehicleReg), eq(1), eq(timeIn), any())).thenReturn(billDTO);

        ResponseEntity<ParkingBillResponse> response = parkingController.getParkingBill(requestData);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(billDTO, response.getBody());
        verify(parkingSpaceService).deallocateParkingSpaceForReg(space);
    }

    // --- GET /parking/summary ---

    @Test
    void getParkingSpaceSummary_adminReturns200WithSummary() {
        ParkingSpaceSummaryResponse summaryDTO = new ParkingSpaceSummaryResponse(List.of());
        when(parkingSpaceService.getParkingSpaceSummary()).thenReturn(summaryDTO);

        ResponseEntity<ParkingSpaceSummaryResponse> response = parkingController.getParkingSpaceSummary();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(summaryDTO, response.getBody());
    }
}