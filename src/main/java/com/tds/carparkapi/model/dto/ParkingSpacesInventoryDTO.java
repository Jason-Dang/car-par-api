package com.tds.carparkapi.model.dto;

public class ParkingSpacesInventoryDTO {
    private Integer availableSpaces;
    private Integer occupiedSpaces;

    public ParkingSpacesInventoryDTO() {}

    public ParkingSpacesInventoryDTO(Integer availableSpaces, Integer occupiedSpaces) {
        this.availableSpaces = availableSpaces;
        this.occupiedSpaces = occupiedSpaces;
    }
}
