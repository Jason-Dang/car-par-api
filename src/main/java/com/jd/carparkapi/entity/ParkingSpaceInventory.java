package com.jd.carparkapi.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="parking_space_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpaceInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="availableSpaces")
    private Integer availableSpaces;

    @Column(name="occupiedSpaces")
    private Integer occupiedSpaces;
}
