package com.nemo.api_health_monitor.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record EndpointResponseDto(
    UUID id,
    String name,
    String url,
    String method,
    int intervalSeconds,
    int expectedStatus,
    boolean isActive,
    Instant createdAt,
    Instant lastCheckedAt,
    Instant nextCheckAt
) {

}
