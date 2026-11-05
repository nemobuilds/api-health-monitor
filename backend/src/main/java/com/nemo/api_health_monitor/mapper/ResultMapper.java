package com.nemo.api_health_monitor.mapper;

import com.nemo.api_health_monitor.domain.dto.ResultResponseDto;
import com.nemo.api_health_monitor.domain.entity.Result;
import org.springframework.stereotype.Component;

@Component
public class ResultMapper {


    public ResultResponseDto toDto(Result result) {
        return new ResultResponseDto(
                result.getId(),
                result.getEndpoint().getId(),
                result.getStatus_code(),
                result.getResponse_time_ms(),
                result.is_up(),
                result.getError_message(),
                result.getChecked_at()
        );
    }
}
