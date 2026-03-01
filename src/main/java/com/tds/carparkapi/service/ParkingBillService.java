package com.tds.carparkapi.service;

import com.tds.carparkapi.model.dto.ParkingBillDTO;
import com.tds.carparkapi.model.entity.ParkingBill;
import com.tds.carparkapi.respository.ParkingBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ParkingBillService {
    @Autowired
    private ParkingBillRepository parkingBillRepository;

    public ParkingBillService(ParkingBillRepository parkingBillRepository) {
        this.parkingBillRepository = parkingBillRepository;
    }

    private ParkingBillDTO mapToParkingBillDTO(ParkingBill parkingBill) {
        return new ParkingBillDTO(
            parkingBill.getId(),
            parkingBill.getVehicleReg(),
            parkingBill.getVehicleCharge(),
            parkingBill.getTimeIn(),
            parkingBill.getTimeOut()
        );
    }

    private BigDecimal getMinuteRate(Integer vehicleType) {
        if (vehicleType == 1) {
            return new BigDecimal(".1");
        }

        if (vehicleType == 2) {
            return new BigDecimal(".2");
        }

        return new BigDecimal(".4");
    }

    public ParkingBillDTO getParkingBill(
        LocalDateTime timeOut,
        String vehicleReg,
        Integer vehicleType,
        LocalDateTime timeIn
    ) {
        Duration diff = Duration.between(timeIn, timeOut);
        BigDecimal minutesStayed = BigDecimal.valueOf(diff.toMinutes());
        BigDecimal surcharge = minutesStayed.divide(BigDecimal.valueOf(5), RoundingMode.FLOOR);
        BigDecimal minuteRate = getMinuteRate(vehicleType);
        BigDecimal vehicleCharge = minutesStayed.multiply(minuteRate).add(surcharge).setScale(2, RoundingMode.HALF_UP);

        return mapToParkingBillDTO(parkingBillRepository.save(
            new ParkingBill(vehicleReg, vehicleCharge.doubleValue(), timeIn, timeOut)
        ));
    }
}
