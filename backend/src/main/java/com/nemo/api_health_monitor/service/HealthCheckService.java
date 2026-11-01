package com.nemo.api_health_monitor.service;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HealthCheckService {

    private final RestClient restClient;

    public HealthCheckService(RestClient restClient) {
        this.restClient = restClient;
    }


    public void checkEndpoint() {
        /* TODO:
        * 1. Learn synchronous RestClient first, then add concurrency using an ExecutorService or Spring's @Async when you need it.
        * 2.  Record Timestamps before and after the request --> use System.nanoTime() for elapsed duration
        * 3. Add error handling: RestClient throws exception for 4xx/5xx errors by default.
        */




        try {
            // Rest Client
            RestClient defaultClient = RestClient.create();

            RestClient customClient = RestClient.builder()
                    .requestFactory(new HttpComponentsClientHttpRequestFactory())
                    .build();
        } catch {

        }
    }


    // Create Check Result Entity
    // Save to database using CheckResultRepository




    // Todo: Add a Response Timeout
}
