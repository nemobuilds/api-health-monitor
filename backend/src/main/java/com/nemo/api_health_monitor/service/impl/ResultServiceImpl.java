package com.nemo.api_health_monitor.service.impl;

import com.nemo.api_health_monitor.domain.dto.ResultResponseDto;
import com.nemo.api_health_monitor.domain.entity.Result;
import com.nemo.api_health_monitor.exception.ResourceNotFoundException;
import com.nemo.api_health_monitor.mapper.ResultMapper;
import com.nemo.api_health_monitor.repository.ResultsRepository;
import com.nemo.api_health_monitor.service.ResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {
    private final ResultsRepository resultsRepository;
    private final ResultMapper resultMapper;

    public ResultServiceImpl (ResultsRepository resultsRepository, ResultMapper resultMapper) {
        this.resultsRepository = resultsRepository;
        this.resultMapper = resultMapper;
    }

    // GET all results
    @Override
    public List<ResultResponseDto> listAllResults() {
        List<Result> results = resultsRepository.findAll();
        return results.stream().map(result -> resultMapper.toDto(result)).toList();
    }

    // GET all results of an endpoint
    @Override
    public List<ResultResponseDto> listAllEndpointResults(UUID endpointId) {
        List<Result> results = resultsRepository.findByEndpoint_Id(endpointId);
        return results.stream().map(result -> resultMapper.toDto(result)).toList();
    }

    // SELECT * FROM endpoint WHERE endpoint.id = endpointId

    // GET id
    @Override
    public ResultResponseDto getResult(UUID id) {
        Result result = resultsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found with id: " + id
                ));
        return resultMapper.toDto(result);
    }

    // DELETE
    @Override
    public void deleteResult(UUID id) {
        Result result = resultsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Result not found with id: " + id
                ));

        resultsRepository.delete(result);
    }
}
