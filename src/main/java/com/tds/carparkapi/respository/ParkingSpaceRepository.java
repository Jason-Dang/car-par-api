package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    ParkingSpace findOneByVehicleReg(String vehicleReg);
}
