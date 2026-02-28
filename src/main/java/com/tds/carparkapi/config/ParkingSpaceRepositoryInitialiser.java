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

import java.util.ArrayList;
import java.util.List;

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
    public ParkingSpaceInventory initialiseParkingSpaceInventory() {
        return parkingSpaceInventoryRepository.save(new ParkingSpaceInventory(totalSpaces, 0));
    }

    @Bean
    @EventListener(ApplicationReadyEvent.class)
    public List<ParkingSpace> initialiseAvailableParkingSpaces() {
        List<ParkingSpace> parkingSpaceList = new ArrayList<>();

        for (int i = 0; i < totalSpaces; i++) {
            parkingSpaceList.add(parkingSpaceRepository.save(new ParkingSpace()));
        }

        return parkingSpaceList;
    }
}
