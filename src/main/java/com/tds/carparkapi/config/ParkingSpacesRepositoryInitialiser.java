package com.tds.carparkapi.config;

import com.tds.carparkapi.model.dto.ParkingSpacesInventoryDTO;
import com.tds.carparkapi.model.entity.ParkingSpace;
import com.tds.carparkapi.model.entity.ParkingSpacesInventory;
import com.tds.carparkapi.respository.ParkingSpaceInventoryRepository;
import com.tds.carparkapi.respository.ParkingSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class ParkingSpacesRepositoryInitialiser {
    @Value("${app.config.totalSpaces}")
    private Integer totalSpaces;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingSpaceInventoryRepository parkingSpaceInventoryRepository;

    @Bean
    @EventListener(ApplicationReadyEvent.class)
    public ParkingSpacesInventoryDTO initialiseParkingSpacesInventory() {
        for (int i = 0; i < totalSpaces; i++) {
            ParkingSpace initialisedParkingSpace = new ParkingSpace();
            parkingSpaceRepository.save(initialisedParkingSpace);
        }

        ParkingSpacesInventory parkingSpacesInventory = new ParkingSpacesInventory(totalSpaces, 0);
        parkingSpaceInventoryRepository.save(parkingSpacesInventory);

        return new ParkingSpacesInventoryDTO(totalSpaces, 0);
    }
}
