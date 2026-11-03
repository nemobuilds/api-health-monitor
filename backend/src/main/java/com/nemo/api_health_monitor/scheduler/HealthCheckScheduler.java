package com.nemo.api_health_monitor.scheduler;

import com.nemo.api_health_monitor.domain.entity.Endpoint;
import com.nemo.api_health_monitor.repository.EndpointRepository;
import com.nemo.api_health_monitor.service.HealthCheckService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;


public class HealthCheckScheduler {
    // Query Active Endpoints
    private final HealthCheckService healthCheckService;
    private final EndpointRepository endpointRepository;

    public HealthCheckScheduler(HealthCheckService healthCheckService, EndpointRepository endpointRepository) {
        this.healthCheckService = healthCheckService;
        this.endpointRepository = endpointRepository;
    }

    @Scheduled(fixedDelay = 10_000)
    public void monitoredEndpoints() {
//        List<Endpoint> endpoints = endpointRepository.findByIsActiveTrue();
//
//        // One caveat: a simple sequential loop can become slow if many endpoints time out. Later, you can introduce bounded concurrency, batching, and database pagination.
//        for (Endpoint endpoint : endpoints) {
//            if (isDue(endpoint)) {
//                healthCheckService.checkEndpoint(endpoint);
//            }
//        }

        List<Endpoint> dueEndpoints =
                endpointRepository.findDueEndpoints(
                        Instant.now(),
                         PageRequest.of(0, 5)
                );

        for (Endpoint endpoint: dueEndpoints) {
            healthCheckService.checkEndpoint(endpoint);
        }
    }



    // Thread Count Control: how many jobs or threads will run check endpoint each time or like batch processing
}
