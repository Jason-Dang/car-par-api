package com.tds.carparkapi.respository;

import com.tds.carparkapi.model.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long> {

}
