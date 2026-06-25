package com.jd.carparkapi.service;

import com.jd.carparkapi.dto.ParkResponse;
import com.jd.carparkapi.dto.ParkingSpacesResponse;
import com.jd.carparkapi.dto.ParkingSpaceSummaryResponse;
import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseErrorException;
import com.jd.carparkapi.exceptionhandling.customexceptions.ResourceNotFoundException;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceServiceUnitTest {

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @InjectMocks
    private ParkingSpaceService parkingSpaceService;

    @AfterEach
    void reset() {
        Mockito.reset(parkingSpaceRepository);
        Mockito.reset(parkingSpaceInventoryRepository);
    }

    // --- getParkingSpaceInventory ---

    @Test
    void getParkingSpaceInventory_returnsDTO() {
        ParkingSpaceInventory inventory = ParkingSpaceInventory.builder()
                .availableSpaces(20)
                .occupiedSpaces(3)
                .build();

        when(parkingSpaceInventoryRepository.findOneById(1L)).thenReturn(inventory);

        ParkingSpacesResponse result = parkingSpaceService.getParkingSpaceInventory();

        Assertions.assertEquals(20, result.availableSpaces());
        Assertions.assertEquals(3, result.occupiedSpaces());
    }

    @Test
    void getParkingSpaceInventory_throwsResourceNotFoundWhenInventoryMissing() {
        when(parkingSpaceInventoryRepository.findOneById(1L)).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class,
            () -> parkingSpaceService.getParkingSpaceInventory());
    }

    // --- getAllocatedParkingSpace ---

    @Test
    void getAllocatedParkingSpace_returnsSpaceWhenFound() {
        ParkingSpace space = mock(ParkingSpace.class);
        when(parkingSpaceRepository.findOneByVehicleReg("ABC123")).thenReturn(space);

        ParkingSpace result = parkingSpaceService.getAllocatedParkingSpace("ABC123");

        Assertions.assertEquals(space, result);
    }

    @Test
    void getAllocatedParkingSpace_returnsNullWhenNotFound() {
        when(parkingSpaceRepository.findOneByVehicleReg("ABC123")).thenReturn(null);

        Assertions.assertNull(parkingSpaceService.getAllocatedParkingSpace("ABC123"));
    }

    // --- allocateNextAvailableParkingSpace ---

    @Test
    void allocateNextAvailableParkingSpace_returnsOccupiedDTO() {
        String vehicleReg = "ABC123";
        int vehicleType = 1;
        LocalDateTime timeIn = LocalDateTime.now(ZoneOffset.UTC);

        ParkingSpace availableSpace = mock(ParkingSpace.class);

        ParkingSpace savedSpace = mock(ParkingSpace.class);
        when(savedSpace.getId()).thenReturn(1L);
        when(savedSpace.getVehicleReg()).thenReturn(vehicleReg);
        when(savedSpace.getTimeIn()).thenReturn(timeIn);

        ParkingSpaceInventory inventory = ParkingSpaceInventory.builder()
                .availableSpaces(20)
                .occupiedSpaces(0)
                .build();

        when(parkingSpaceRepository.findNextAvailableParkingSpace()).thenReturn(availableSpace);
        when(parkingSpaceRepository.save(availableSpace)).thenReturn(savedSpace);
        when(parkingSpaceInventoryRepository.findOneById(1L)).thenReturn(inventory);

        ParkResponse result = parkingSpaceService.allocateNextAvailableParkingSpace(
            vehicleReg,
            vehicleType,
            null,
            null
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.spaceNumber());
        Assertions.assertEquals(vehicleReg, result.vehicleReg());
        Assertions.assertEquals(timeIn, result.timeIn());
    }

    @Test
    void allocateNextAvailableParkingSpace_throwsResourceNotFoundWhenNoSpaceAvailable() {
        when(parkingSpaceRepository.findNextAvailableParkingSpace()).thenReturn(null);

        Assertions.assertThrows(ResourceNotFoundException.class,
            () -> parkingSpaceService.allocateNextAvailableParkingSpace(
                "ABC123",
                1,
                null,
                null
            ));
    }

    @Test
    void allocateNextAvailableParkingSpace_throwsDatabaseErrorWhenSavedSpaceHasNoId() {
        ParkingSpace availableSpace = mock(ParkingSpace.class);
        ParkingSpace savedSpaceWithNoId = mock(ParkingSpace.class);
        when(savedSpaceWithNoId.getId()).thenReturn(null);

        when(parkingSpaceRepository.findNextAvailableParkingSpace()).thenReturn(availableSpace);
        when(parkingSpaceRepository.save(availableSpace)).thenReturn(savedSpaceWithNoId);

        Assertions.assertThrows(DatabaseErrorException.class,
            () -> parkingSpaceService.allocateNextAvailableParkingSpace(
                "ABC123",
                1,
                null,
                null
            ));
    }

    // --- deallocateParkingSpaceForReg ---

    @Test
    void deallocateParkingSpaceForReg_clearsParkingSpaceFields() {
        ParkingSpace space = mock(ParkingSpace.class);
        when(parkingSpaceRepository.save(space)).thenReturn(space);

        ParkingSpaceInventory inventory = ParkingSpaceInventory.builder()
                .availableSpaces(19)
                .occupiedSpaces(1)
                .build();
        when(parkingSpaceInventoryRepository.findOneById(1L)).thenReturn(inventory);

        parkingSpaceService.deallocateParkingSpaceForReg(space);

        verify(space).setVehicleReg(null);
        verify(space).setVehicleType(null);
        verify(space).setTimeIn(null);
        verify(parkingSpaceRepository).save(space);
    }

    @Test
    void deallocateParkingSpaceForReg_updatesInventory() {
        ParkingSpace space = mock(ParkingSpace.class);
        when(parkingSpaceRepository.save(space)).thenReturn(space);

        ParkingSpaceInventory inventory = ParkingSpaceInventory.builder()
                .availableSpaces(19)
                .occupiedSpaces(1)
                .build();
        when(parkingSpaceInventoryRepository.findOneById(1L)).thenReturn(inventory);

        parkingSpaceService.deallocateParkingSpaceForReg(space);

        Assertions.assertEquals(20, inventory.getAvailableSpaces());
        Assertions.assertEquals(0, inventory.getOccupiedSpaces());
    }

    // --- getParkingSpaceSummary ---

    @Test
    void getParkingSpaceSummary_returnsEmptySummaryWhenNoParkingSpaces() {
        when(parkingSpaceRepository.findAll()).thenReturn(List.of());

        ParkingSpaceSummaryResponse result = parkingSpaceService.getParkingSpaceSummary();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.items().isEmpty());
    }

    @Test
    void getParkingSpaceSummary_skipsUnoccupiedSpaces() {
        ParkingSpace occupiedSpace = mock(ParkingSpace.class);
        when(occupiedSpace.getTimeIn()).thenReturn(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(10));
        when(occupiedSpace.getVehicleReg()).thenReturn("ABC123");

        ParkingSpace emptySpace = mock(ParkingSpace.class);

        when(parkingSpaceRepository.findAll()).thenReturn(List.of(occupiedSpace, emptySpace));

        ParkingSpaceSummaryResponse result = parkingSpaceService.getParkingSpaceSummary();

        Assertions.assertEquals(1, result.items().size());
        Assertions.assertEquals("ABC123", result.items().getFirst().vehicleReg());
    }

    @Test
    void getParkingSpaceSummary_includesAllOccupiedSpaces() {
        ParkingSpace spaceA = mock(ParkingSpace.class);
        when(spaceA.getTimeIn()).thenReturn(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(5));
        when(spaceA.getVehicleReg()).thenReturn("AAA111");

        ParkingSpace spaceB = mock(ParkingSpace.class);
        when(spaceB.getTimeIn()).thenReturn(LocalDateTime.now(ZoneOffset.UTC).minusMinutes(15));
        when(spaceB.getVehicleReg()).thenReturn("BBB222");

        when(parkingSpaceRepository.findAll()).thenReturn(List.of(spaceA, spaceB));

        ParkingSpaceSummaryResponse result = parkingSpaceService.getParkingSpaceSummary();

        Assertions.assertEquals(2, result.items().size());
        Assertions.assertEquals("AAA111", result.items().get(0).vehicleReg());
        Assertions.assertEquals("BBB222", result.items().get(1).vehicleReg());
    }
}