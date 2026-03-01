package com.tds.carparkapi.Respository;

import com.tds.carparkapi.Entity.ParkingSpaceInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceInventoryRepository extends JpaRepository<ParkingSpaceInventory, Long> {
    ParkingSpaceInventory findOneById(Long id);
}
