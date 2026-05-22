package com.jd.carparkapi.config;

import com.jd.carparkapi.entity.ParkingSpace;
import com.jd.carparkapi.entity.ParkingSpaceInventory;
import com.jd.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.jd.carparkapi.respository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class ParkingSpaceRepositoryInitialiser {
    private final Integer totalSpaces;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    public ParkingSpaceRepositoryInitialiser(
            @Value("${app.config.totalSpaces}") Integer totalSpaces,
            ParkingSpaceRepository parkingSpaceRepository,
            ParkingSpaceInventoryRepository parkingSpaceInventoryRepository
    ) {
        this.totalSpaces = totalSpaces;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceInventoryRepository = parkingSpaceInventoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialiseParkingSpaceInventory() {
        if (totalSpaces <= 0) {
            return;
        }

        parkingSpaceInventoryRepository.save(new ParkingSpaceInventory(totalSpaces, 0));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialiseAvailableParkingSpaces() {
        if (totalSpaces <= 0) {
            return;
        }

        for (int i = 0; i < totalSpaces; i++) {
            parkingSpaceRepository.save(new ParkingSpace());
        }
    }
}
