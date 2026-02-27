package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {

}
