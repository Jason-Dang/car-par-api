package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.ParkingSpaceInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceInventoryRepository extends JpaRepository<ParkingSpaceInventory, Long> {
    ParkingSpaceInventory findOneById(Long id);
}
