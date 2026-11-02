package com.nemo.api_health_monitor.service.impl;

import com.nemo.api_health_monitor.domain.dto.EndpointRequestDto;
import com.nemo.api_health_monitor.domain.dto.EndpointResponseDto;
import com.nemo.api_health_monitor.domain.entity.Endpoint;
import com.nemo.api_health_monitor.exception.DuplicateResourceException;
import com.nemo.api_health_monitor.exception.ResourceNotFoundException;
import com.nemo.api_health_monitor.mapper.EndpointMapper;
import com.nemo.api_health_monitor.repository.EndpointRepository;
import com.nemo.api_health_monitor.repository.ResultsRepository;
import com.nemo.api_health_monitor.service.EndpointService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class EndpointServiceImpl implements EndpointService {
    private final EndpointRepository endpointRepository;
    private final ResultsRepository resultsRepository;
    private final EndpointMapper endpointMapper;

//    Concstuctor
    public EndpointServiceImpl(EndpointRepository endpointRepository, ResultsRepository resultsRepository, EndpointMapper endpointMapper) {
        this.endpointRepository = endpointRepository;
        this.resultsRepository = resultsRepository;
        this.endpointMapper = endpointMapper;
    }

//    GET ID
    @Override
    public EndpointResponseDto getEndpoint(UUID id) {
        Endpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endpoint not found with id: " + id
                ));
        return endpointMapper.toDto(endpoint);
    }

//    GET ALL
    @Override
    public List<EndpointResponseDto> listAllEndpoints() {
        List<Endpoint> endpoints = endpointRepository.findAll();

        return endpoints.stream().map(endpoint -> endpointMapper.toDto(endpoint)).toList();

//        Statement Lambda version:

//        return endpoints.stream().map(endpoint -> {
//            return endpointMapper.toDto(endpoint);
//        }).toList();
    }

//    POST   /api/endpoints          Register a new endpoint to monitor
    @Override
    public EndpointResponseDto addNewEndpoint(EndpointRequestDto request) {
        if (endpointRepository.existsByUrlAndMethod(request.url(), request.method())){
            throw new DuplicateResourceException(
                    "Endpoint with url '" + request.url() + "' already exists with the method '" + request.method() + "'"
            );
        }
        Endpoint endpoint = endpointMapper.toEntity(request);
        endpoint = endpointRepository.save(endpoint);

        return endpointMapper.toDto(endpoint);
    }


//    PUT    /api/endpoints/{id}     Update endpoint (URL, interval, etc.)
    @Override
    public EndpointResponseDto updateEndpoint(EndpointRequestDto request, UUID id) {
//        if (endpointRepository.existsById(id)){
//            throw new ResourceNotFoundException(
//                    "Endpoint doesn't exist"
//            );
//        }

        Endpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                   "Endpoint doesn't exist"
                ));

        endpoint.setName(request.name());
        endpoint.setUrl(request.url());
        endpoint.setMethod(request.method());
        endpoint.setIntervalSeconds(request.intervalSeconds());
        endpoint.setExpectedStatus(request.expectedStatus());
        endpoint.setActive(request.active());

        endpoint = endpointRepository.save(endpoint);

        return endpointMapper.toDto(endpoint);
    }


//    PATCH  /api/endpoints/{id}/toggle   Turn monitoring on or off
    @Override
    public EndpointResponseDto toggleMonitoring(UUID id) {
        Endpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endpoint not found with id: " + id
                ));

        endpoint.setActive(!endpoint.isActive());

        endpointRepository.save(endpoint);

        return endpointMapper.toDto(endpoint);
    }


    //    DELETE /api/endpoints/{id}     Stop monitoring and delete
    @Override
    public void deleteEndpoint(UUID id) {
        Endpoint endpoint = endpointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endpoint not found with id: " + id
                ));

        // Delete all results records from database
        resultsRepository.deleteAllByEndpointId(id);

        // Delete the endpoint in the database
        endpointRepository.delete(endpoint);
    }
}
