package com.nemo.api_health_monitor.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record EndpointRequestDto(
        @NotBlank(message = "Endpoint name is required")
        String name,
        @NotBlank(message = "URL is required")
        String url,
        @NotBlank(message = "Method is required")
        String method,
        int intervalSeconds,
        int expectedStatus,
        boolean isActive
) {
}
