package com.nemo.api_health_monitor.scheduler;

import com.nemo.api_health_monitor.domain.entity.Endpoint;
import com.nemo.api_health_monitor.repository.EndpointRepository;
import com.nemo.api_health_monitor.service.HealthCheckService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
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
        System.out.println("Scheduler running: " + Instant.now());

        // finds due endpoints in the database
        List<Endpoint> dueEndpoints =
                endpointRepository.findDueEndpoints(
                        Instant.now(),
                         // pagination for getting 5 endpoints at a time
                         PageRequest.of(0, 1)
                );

        // loop through all (5) the dueEndpoints one by one and run healthCheckService:
        for (Endpoint endpoint: dueEndpoints) {
            healthCheckService.checkEndpoint(endpoint);
        }
    }

    // Thread Count Control: how many jobs or threads will run check endpoint each time or like batch processing
}
