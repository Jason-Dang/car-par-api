package com.tds.carparkapi.config;

import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.entity.ParkingSpaceInventory;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class ParkingSpaceRepositoryInitialiser {
    private final Integer totalSpaces;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @Autowired
    public ParkingSpaceRepositoryInitialiser(
            @Value("${app.config.totalSpaces}") Integer totalSpaces,
            ParkingSpaceRepository parkingSpaceRepository,
            ParkingSpaceInventoryRepository parkingSpaceInventoryRepository
    ) {
        this.totalSpaces = totalSpaces;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.parkingSpaceInventoryRepository = parkingSpaceInventoryRepository;
    }

    @Bean
    @EventListener(ApplicationReadyEvent.class)
    public boolean initialiseParkingSpaceInventory() {
        if (totalSpaces <= 0) {
            return false;
        }

        parkingSpaceInventoryRepository.save(new ParkingSpaceInventory(totalSpaces, 0));

        return true;
    }

    @Bean
    @EventListener(ApplicationReadyEvent.class)
    public boolean initialiseAvailableParkingSpaces() {
        if (totalSpaces <= 0) {
            return false;
        }

        for (int i = 0; i < totalSpaces; i++) {
            parkingSpaceRepository.save(new ParkingSpace());
        }

        return true;
    }
}
