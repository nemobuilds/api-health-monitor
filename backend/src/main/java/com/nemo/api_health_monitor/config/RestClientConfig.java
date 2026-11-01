package com.nemo.api_health_monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration // Tells Spring this class provides application configuration.
public class RestClientConfig {

    @Bean // Registers RestClient instance in Spring's application configuration
    public RestClient restClient(RestClient.Builder builder) { // RestClient.builder constructs and configures the HTTP Client.
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(Duration.ofSeconds(5)); // Limits how long establishing a connection can take.
        factory.setReadTimeout(Duration.ofSeconds(5)); // limits waiting for response data

        return builder
                .requestFactory(factory)
                .build();

    }
}
