package com.tds.carparkapi.service;

import com.tds.carparkapi.respository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceService {
    @Autowired
    private SpaceRepository spaceRepository;
}
