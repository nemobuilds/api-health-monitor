package com.nemo.api_health_monitor.domain.dto;


import java.time.Instant;
import java.util.UUID;

public record ResultResponseDto(
    UUID id,
    UUID endpointId,
    Integer statusCode,
    Long responseTimeMs,
    boolean isUp,
    String errorMessage,
    Instant checkedAt
) {
}
