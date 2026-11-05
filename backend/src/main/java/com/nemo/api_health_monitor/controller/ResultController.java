package com.nemo.api_health_monitor.controller;

import com.nemo.api_health_monitor.domain.dto.ResultResponseDto;
import com.nemo.api_health_monitor.domain.entity.Result;
import com.nemo.api_health_monitor.service.ResultService;
import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping
    public ResponseEntity<List<ResultResponseDto>> listAllResults() {
        return ResponseEntity.ok(resultService.listAllResults());
    }

    // TODO: Figure out how to get all results with a specific endpoint
    @GetMapping("/{endpointId}")
    public ResponseEntity<List<ResultResponseDto>> listAllEndpointResults(@PathVariable UUID endpointId) {
        return ResponseEntity.ok(resultService.listAllEndpointResults(endpointId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto> getResult(@PathVariable UUID id) {
        return ResponseEntity.ok(resultService.getResult(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable UUID id){
        resultService.deleteResult(id);
        return ResponseEntity.noContent().build();
    }
}
