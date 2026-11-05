package com.nemo.api_health_monitor.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

//id                UUID, primary key
//        endpoint_id       UUID, foreign key → monitored_endpoints
//status_code       INTEGER       actual HTTP status returned
//response_time_ms  LONG          how long the request took in ms
//is_up             BOOLEAN       did it match expected_status
//checked_at        TIMESTAMP
//error_message     VARCHAR       null if successful, error if failed

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private Endpoint endpoint;

    @Column(nullable = false)
    private int status_code;

    @Column(nullable = false)
    private Long response_time_ms;

    @Column(nullable = false)
    private boolean is_up;

    @Column
    private Instant checked_at; // changes when someone check the result

    @Column
    private String error_message;
}
