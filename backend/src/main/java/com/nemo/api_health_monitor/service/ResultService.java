package com.nemo.api_health_monitor.service;

import com.nemo.api_health_monitor.domain.dto.ResultResponseDto;

import java.util.List;
import java.util.UUID;

public interface ResultService {
    List<ResultResponseDto> listAllResults();
    List<ResultResponseDto> listAllEndpointResults(UUID id);
    ResultResponseDto getResult(UUID id);
    void deleteResult(UUID id);
}
