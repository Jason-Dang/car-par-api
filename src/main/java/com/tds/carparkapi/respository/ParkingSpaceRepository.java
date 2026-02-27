package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    ParkingSpace findOneByVehicleReg(String vehicleReg);

    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.vehicleReg IS NULL ORDER BY ps.id LIMIT 1")
    ParkingSpace findNextAvailableParkingSpace();
}
