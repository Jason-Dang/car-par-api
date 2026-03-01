package com.tds.carparkapi.DTO;

public class ParkingSpaceInventoryDTO {
    private Integer availableSpaces;
    private Integer occupiedSpaces;

    public ParkingSpaceInventoryDTO() {}

    public ParkingSpaceInventoryDTO(Integer availableSpaces, Integer occupiedSpaces) {
        this.availableSpaces = availableSpaces;
        this.occupiedSpaces = occupiedSpaces;
    }

    public Integer getAvailableSpaces() {
        return this.availableSpaces;
    }

    public Integer getOccupiedSpaces() {
        return this.occupiedSpaces;
    }
}
