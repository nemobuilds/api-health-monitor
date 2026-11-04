package com.nemo.api_health_monitor.service;

import com.nemo.api_health_monitor.domain.entity.Endpoint;
import com.nemo.api_health_monitor.domain.entity.Result;
import com.nemo.api_health_monitor.repository.ResultsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class HealthCheckService {
    /* TODO:
     * 1. Learn synchronous RestClient first, then add concurrency using an ExecutorService or Spring's @Async when you need it.
     */

    private final RestClient restClient;
    private final ResultsRepository resultsRepository;

    public HealthCheckService(RestClient restClient, ResultsRepository resultsRepository) {
        // Spring checks the Spring Application Context if there's an existing 'RestClient' bean
        this.restClient = restClient;
        this.resultsRepository = resultsRepository;
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
            // We only need the status code, not the response body:
//            ResponseEntity<Void> response = restClient
//                    // HTTP Method: Get
//                    .get()
//                    // Target endpoint
//                    .uri(url)
//                    // Prepares to extract the HTTP Response
//                    .retrieve()
//                    // Executes the request and returns status and headers without deserializing the body
//                    .toBodilessEntity();

            //  Capture status code or error
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

//        } catch (RestClientResponseException e){
//            // Handle Request failures without HTTP Response
//
//            // Server responded with an HTTP error (e.g., 404, 500)
//            int statusCode = e.getStatusCode().value();
//            System.out.println("HTTP error: " + statusCode);
//
//       } catch (ResourceAccessException e) {
//            // Connection refused, timeout, DNS failure, etc.
//            System.out.println("Connection error: " + e.getMessage());

        }  catch (RestClientException e) {
            // Other RestClient-related failures
            System.out.println("RestClient error: " + e.getMessage());
        }

        // Calculate response time
        result.setResponse_time_ms(elapsed / 1_000_000);

        // 7. Build CheckResult entity
        result.setEndpoint(endpoint);

        // 8. Save using CheckResultRepository
        resultsRepository.save(result);
    }


    // Create Check Result Entity
    // Save to database using CheckResultRepository




    // Todo: Add a Response Timeout
}
