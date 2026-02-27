package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.ParkingSpacesInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceInventoryRepository extends JpaRepository<ParkingSpacesInventory, Long> {
    ParkingSpacesInventory findOneById(Long id);
}
