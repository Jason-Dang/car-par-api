package com.tds.carparkapi.Respository;

import com.tds.carparkapi.Entity.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {

}
