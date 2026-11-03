package com.nemo.api_health_monitor.repository;

import com.nemo.api_health_monitor.domain.entity.Endpoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EndpointRepository extends JpaRepository<Endpoint, UUID> {
    Optional<Endpoint> findByName(String name); // Spring Data JPA automatically implements this based on the method name — it generates `SELECT * FROM buckets WHERE name = ?`

    List<Endpoint> findByIsActiveTrue();

    boolean existsByUrl(String url); // Same pattern, returns true/false

    boolean existsById(UUID id);

    boolean existsByUrlAndMethod(String url, String method);

    @Query("""
        SELECT e FROM Endpoint e
        WHERE e.isActive = true
          AND e.nextCheckAt <= :now
        ORDER BY e.nextCheckAt ASC
    """)
    List<Endpoint> findDueEndpoints(
            @Param("now")Instant now,
            Pageable pageable
    );
}
