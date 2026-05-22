package com.jd.carparkapi.respository;

import com.jd.carparkapi.entity.ParkingBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingBillRepository extends JpaRepository<ParkingBill, Long> {

}
