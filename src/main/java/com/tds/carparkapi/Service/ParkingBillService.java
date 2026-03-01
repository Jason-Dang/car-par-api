package com.tds.carparkapi.Service;

import com.tds.carparkapi.DTO.ParkingBillDTO;
import com.tds.carparkapi.Entity.ParkingBill;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.DatabaseConnectionException;
import com.tds.carparkapi.ExceptionHandling.CustomExceptions.InvalidDataException;
import com.tds.carparkapi.Respository.ParkingBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        if (vehicleType == 3) {
            return new BigDecimal(".4");
        }

        throw new InvalidDataException("Vehicle type must be either: '1', '2' or '3'", "err-ps4", HttpStatus.BAD_REQUEST);
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

        ParkingBill parkingBill = new ParkingBill(vehicleReg, vehicleCharge.doubleValue(), timeIn, timeOut);

        try {
            return mapToParkingBillDTO(parkingBillRepository.save(parkingBill));
        } catch (Exception e) {
            throw new DatabaseConnectionException("Unable to save parking bill in the database", "err-db1" , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
