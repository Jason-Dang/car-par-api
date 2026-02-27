package com.tds.carparkapi.model.entity;

import jakarta.persistence.*;

@Entity
public class ParkingSpacesInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="availableSpaces")
    private Integer availableSpaces;

    @Column(name="occupiedSpaces")
    private Integer occupiedSpaces;

    protected ParkingSpacesInventory() {}

    public ParkingSpacesInventory(Integer availableSpaces, Integer occupiedSpaces) {
        this.availableSpaces = availableSpaces;
        this.occupiedSpaces = occupiedSpaces;
    }

    public Integer getAvailableSpaces() {
        return this.availableSpaces;
    }

    public Integer getOccupiedSpaces() {
        return this.occupiedSpaces;
    }

    public void setAvailableSpaces(Integer availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public void setOccupiedSpaces(Integer occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }
}
