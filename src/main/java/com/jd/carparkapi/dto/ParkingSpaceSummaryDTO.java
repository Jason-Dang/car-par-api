package com.jd.carparkapi.dto;

import java.util.List;

public class ParkingSpaceSummaryDTO {
    private List<ParkingSpaceSummaryItemDTO> items;

    public ParkingSpaceSummaryDTO() {}

    public ParkingSpaceSummaryDTO(List<ParkingSpaceSummaryItemDTO> items) {
        this.items = items;
    }

    public List<ParkingSpaceSummaryItemDTO> getItems() {
        return this.items;
    }
}
