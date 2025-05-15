package com.senla.aggregator.mapper;

import com.senla.aggregator.dto.JobInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.batch.core.JobExecution;

@Mapper(componentModel = "spring")
public interface JobExecutionMapper {

    @Mapping(source = "jobInstance.jobName", target = "jobName")
    @Mapping(source = "exitStatus.exitCode", target = "exitStatus")
    JobInfoDto toJobInfoDto(JobExecution jobExecution);
}
