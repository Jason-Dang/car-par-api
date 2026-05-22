package com.jd.carparkapi.respository;

import com.jd.carparkapi.entity.ParkingSpaceInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceInventoryRepository extends JpaRepository<ParkingSpaceInventory, Long> {
    ParkingSpaceInventory findOneById(Long id);
}
