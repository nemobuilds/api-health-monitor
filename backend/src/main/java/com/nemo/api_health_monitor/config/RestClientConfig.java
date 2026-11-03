package com.nemo.api_health_monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration // Tells Spring this class provides application configuration.
public class RestClientConfig {

    // Bean: Registers RestClient instance in Spring's application configuration
    @Bean
    // RestClient.builder constructs and configures the HTTP Client.
    public RestClient restClient(RestClient.Builder builder) {

        // Configure Request Factory
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // Limits how long "establishing a connection" can take.
        factory.setConnectTimeout(Duration.ofSeconds(60));
        // limits waiting for response data
        factory.setReadTimeout(Duration.ofSeconds(60));

        return builder
            .requestFactory(factory)
            .build();

    }
}
