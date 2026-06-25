package com.jd.carparkapi.service;

import com.jd.carparkapi.dto.ParkingBillResponse;
import com.jd.carparkapi.entity.ParkingBill;
import com.jd.carparkapi.exceptionhandling.customexceptions.DatabaseConnectionException;
import com.jd.carparkapi.exceptionhandling.customexceptions.InvalidDataException;
import com.jd.carparkapi.respository.ParkingBillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class ParkingBillService {
    private final ParkingBillRepository parkingBillRepository;

    public ParkingBillService(ParkingBillRepository parkingBillRepository) {
        this.parkingBillRepository = parkingBillRepository;
    }

    private ParkingBillResponse mapToParkingBillDTO(ParkingBill parkingBill) {
        return new ParkingBillResponse(
            parkingBill.getId(),
            parkingBill.getVehicleReg(),
            parkingBill.getVehicleCharge(),
            parkingBill.getTimeIn(),
            parkingBill.getTimeOut()
        );
    }

    private BigDecimal getMinuteRate(Integer vehicleType) {
        return switch (vehicleType) {
            case 1 -> BigDecimal.valueOf(0.1);
            case 2 -> BigDecimal.valueOf(0.2);
            case 3 -> BigDecimal.valueOf(0.4);
            default -> throw new InvalidDataException(
                "Vehicle type must be either: '1', '2' or '3'",
                "err-ps4",
                HttpStatus.BAD_REQUEST
            );
        };
    }

    public ParkingBillResponse getParkingBill(
        String vehicleReg,
        Integer vehicleType,
        LocalDateTime timeIn,
        LocalDateTime timeOut
    ) {
        ZonedDateTime timeInZoned = ZonedDateTime.of(timeIn, ZoneId.of("UTC"));
        ZonedDateTime timeOutZoned = ZonedDateTime.of(timeOut, ZoneId.of("UTC"));

        Duration diff = Duration.between(timeInZoned, timeOutZoned);
        BigDecimal minutesStayed = BigDecimal.valueOf(diff.toMinutes());
        BigDecimal surcharge = minutesStayed.divide(BigDecimal.valueOf(5), RoundingMode.FLOOR);
        BigDecimal minuteRate = getMinuteRate(vehicleType);
        BigDecimal vehicleCharge = minutesStayed.multiply(minuteRate)
            .add(surcharge)
            .setScale(2, RoundingMode.HALF_UP);

        ParkingBill parkingBill = new ParkingBill(vehicleReg, vehicleCharge.doubleValue(), timeIn, timeOut);

        try {
            return mapToParkingBillDTO(parkingBillRepository.save(parkingBill));
        } catch (Exception _) {
            throw new DatabaseConnectionException(
                "Unable to save parking bill in the database",
                "err-db1",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
