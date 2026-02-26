package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

}
