package com.tds.carparkapi.controller;

import com.tds.carparkapi.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parking")
public class SpaceController
{
    @Autowired
    private SpaceService spaceService;
}
