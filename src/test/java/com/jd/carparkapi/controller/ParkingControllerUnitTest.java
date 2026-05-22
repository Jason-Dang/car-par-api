package com.jd.carparkapi.controller;

import com.jd.carparkapi.dto.OccupiedParkingSpaceDTO;
import com.jd.carparkapi.dto.ParkingBillDTO;
import com.jd.carparkapi.dto.ParkingSpaceInventoryDTO;
import com.jd.carparkapi.dto.ParkingSpaceSummaryDTO;
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
        ParkingSpaceInventoryDTO inventory = new ParkingSpaceInventoryDTO(17, 3);
        when(parkingSpaceService.getParkingSpaceInventory()).thenReturn(inventory);

        ResponseEntity<ParkingSpaceInventoryDTO> response = parkingController.getParkingSpaceInventory();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(inventory, response.getBody());
    }

    // --- POST /parking ---

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegKeyMissing() {
        Map<String, Object> requestData = Map.of("vehicleType", 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleTypeKeyMissing() {
        Map<String, Object> requestData = Map.of("vehicleReg", "ABC123");

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegIsNull() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("vehicleReg", null);
        requestData.put("vehicleType", 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleTypeIsNull() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("vehicleReg", "ABC123");
        requestData.put("vehicleType", null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleRegIsEmpty() {
        Map<String, Object> requestData = Map.of("vehicleReg", "", "vehicleType", 1);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_throwsWhenVehicleAlreadyParked() {
        String vehicleReg = "ABC123";
        Map<String, Object> requestData = Map.of("vehicleReg", vehicleReg, "vehicleType", 1);

        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(mock(ParkingSpace.class));

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getNextAvailableParkingSpace(requestData));
    }

    @Test
    void getNextAvailableParkingSpace_returns200WithOccupiedSpaceDTO() {
        String vehicleReg = "ABC123";
        int vehicleType = 1;
        Map<String, Object> requestData = Map.of("vehicleReg", vehicleReg, "vehicleType", vehicleType);

        OccupiedParkingSpaceDTO occupiedDTO = new OccupiedParkingSpaceDTO(5L, vehicleReg, LocalDateTime.now());
        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(null);
        when(parkingSpaceService.allocateNextAvailableParkingSpace(vehicleReg, vehicleType)).thenReturn(occupiedDTO);

        ResponseEntity<OccupiedParkingSpaceDTO> response = parkingController.getNextAvailableParkingSpace(requestData);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(occupiedDTO, response.getBody());
    }

    // --- POST /parking/bill ---

    @Test
    void getParkingBill_throwsWhenVehicleRegKeyMissing() {
        Map<String, Object> requestData = Map.of();

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_throwsWhenVehicleRegIsNull() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("vehicleReg", null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_throwsWhenVehicleRegIsEmpty() {
        Map<String, Object> requestData = Map.of("vehicleReg", "");

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_throwsWhenVehicleNotParked() {
        String vehicleReg = "ABC123";
        Map<String, Object> requestData = Map.of("vehicleReg", vehicleReg);

        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(null);

        Assertions.assertThrows(InvalidDataException.class,
            () -> parkingController.getParkingBill(requestData));
    }

    @Test
    void getParkingBill_returns200WithBillDTO() {
        String vehicleReg = "ABC123";
        Map<String, Object> requestData = Map.of("vehicleReg", vehicleReg);

        LocalDateTime timeIn = LocalDateTime.now().minusMinutes(30);
        ParkingSpace space = mock(ParkingSpace.class);
        when(space.getVehicleType()).thenReturn(1);
        when(space.getTimeIn()).thenReturn(timeIn);

        ParkingBillDTO billDTO = new ParkingBillDTO(1L, vehicleReg, 9.0, timeIn, LocalDateTime.now());
        when(parkingSpaceService.getAllocatedParkingSpace(vehicleReg)).thenReturn(space);
        when(parkingBillService.getParkingBill(eq(vehicleReg), eq(1), eq(timeIn), any())).thenReturn(billDTO);

        ResponseEntity<ParkingBillDTO> response = parkingController.getParkingBill(requestData);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(billDTO, response.getBody());
        verify(parkingSpaceService).deallocateParkingSpaceForReg(space);
    }

    // --- GET /parking/summary ---

    @Test
    void getParkingSpaceSummary_adminReturns200WithSummary() {
        ParkingSpaceSummaryDTO summaryDTO = new ParkingSpaceSummaryDTO(List.of());
        when(parkingSpaceService.getParkingSpaceSummary()).thenReturn(summaryDTO);

        ResponseEntity<ParkingSpaceSummaryDTO> response = parkingController.getParkingSpaceSummary();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(summaryDTO, response.getBody());
    }
}