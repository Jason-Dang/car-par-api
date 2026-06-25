package com.jd.carparkapi.dto;

import java.util.List;

public record ParkingSpaceSummaryResponse(List<ParkingSpaceSummaryItemResponse> items) {}