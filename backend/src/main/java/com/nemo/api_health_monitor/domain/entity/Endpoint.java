package com.nemo.api_health_monitor.domain.entity;

//id               UUID, primary key
//name             VARCHAR       "Production Auth API"
//url              VARCHAR       "https://api.example.com/health"
//method           VARCHAR       "GET"
//interval_seconds INTEGER       how often to ping (e.g. 60)
//expected_status  INTEGER       expected HTTP status code (e.g. 200)
//is_active        BOOLEAN       whether monitoring is on or off
//created_at       TIMESTAMP
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "endpoints",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_endpoint_url_method",
                columnNames = {"url", "method"}
        )

)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // This is OPTIONAL
    @OneToMany(mappedBy = "endpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> checkResults = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private int intervalSeconds;

    @Column(nullable = false)
    private int expectedStatus;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
