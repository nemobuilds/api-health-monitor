package com.nemo.api_health_monitor.service;

import com.nemo.api_health_monitor.domain.entity.Endpoint;
import com.nemo.api_health_monitor.domain.entity.Result;
import com.nemo.api_health_monitor.repository.EndpointRepository;
import com.nemo.api_health_monitor.repository.ResultsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;

@Service
public class HealthCheckService {
    /* TODO:
     * 1. Learn synchronous RestClient first, then add concurrency using an ExecutorService or Spring's @Async when you need it.
     */

    private final RestClient restClient;
    private final ResultsRepository resultsRepository;
    private final EndpointRepository endpointRepository;

    public HealthCheckService(RestClient restClient, ResultsRepository resultsRepository, EndpointRepository endpointRepository) {
        // Spring checks the Spring Application Context if there's an existing 'RestClient' bean
        this.restClient = restClient;
        this.resultsRepository = resultsRepository;
        this.endpointRepository = endpointRepository;
    }

    // Notice we didn't write "RestClient.create()", Spring injects the configured instance through the constructor.
    // this is called "Constructor Dependency Injection"

    public void checkEndpoint(Endpoint endpoint) {

        // Entity:
        Result result = new Result();

        String url = endpoint.getUrl();
        int expectedStatus = endpoint.getExpectedStatus();

        // Start timer
        long start = System.nanoTime();
        long elapsed = 0;

        try {
            //  HTTP Request, Capture status code or error
            int statusCode = restClient.get()
                    .uri(url)
                    .exchange((request, response) ->
                            response.getStatusCode().value()
                    );

            elapsed = System.nanoTime() - start;

            System.out.println("Status: " + statusCode);

            result.setStatus_code(statusCode);
            // Compare actual status with expected status
            result.set_up(statusCode == expectedStatus);

        }  catch (RestClientException e) {
            // Other RestClient-related failures
            System.out.println("RestClient error: " + e.getMessage());
        }

        // Calculate response time
        result.setResponse_time_ms(elapsed / 1_000_000);

        // 7. Build CheckResult entity
        result.setEndpoint(endpoint);

        // Update the lastCheckedAt & nextCheckAt
        Instant checkedAt = Instant.now();
        result.setChecked_at(checkedAt);

        // 8. Save using CheckResultRepository
        resultsRepository.save(result);


        endpoint.setLastCheckedAt(checkedAt);
        endpoint.setNextCheckAt(checkedAt.plusSeconds(endpoint.getIntervalSeconds()));

        endpointRepository.save(endpoint);
    }
}
