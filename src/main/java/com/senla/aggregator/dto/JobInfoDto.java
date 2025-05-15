package com.senla.aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.BatchStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class JobInfoDto {

    @JsonProperty("job_name")
    private String jobName;

    @JsonProperty("status")
    private BatchStatus status;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @JsonProperty("exit_status")
    private String exitStatus;
}
