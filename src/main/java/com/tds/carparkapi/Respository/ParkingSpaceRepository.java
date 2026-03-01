package com.tds.carparkapi.Respository;

import com.tds.carparkapi.Entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    ParkingSpace findOneByVehicleReg(String vehicleReg);

    @Query("SELECT ps FROM ParkingSpace ps WHERE ps.vehicleReg IS NULL ORDER BY ps.id LIMIT 1")
    ParkingSpace findNextAvailableParkingSpace();
}
