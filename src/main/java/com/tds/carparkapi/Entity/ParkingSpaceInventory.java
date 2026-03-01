package com.tds.carparkapi.Entity;

import jakarta.persistence.*;

@Entity
public class ParkingSpaceInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="availableSpaces")
    private Integer availableSpaces;

    @Column(name="occupiedSpaces")
    private Integer occupiedSpaces;

    protected ParkingSpaceInventory() {}

    public ParkingSpaceInventory(Integer availableSpaces, Integer occupiedSpaces) {
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
