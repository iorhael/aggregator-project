package com.senla.aggregator.config.batch.productCard;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class FileCleanerJobExecutionListener implements JobExecutionListener {

    @Override
    @SneakyThrows
    public void afterJob(@NonNull JobExecution jobExecution) {
        String path = jobExecution.getJobParameters().getString("importFile");
        Files.deleteIfExists(Paths.get(Objects.requireNonNull(path)));
    }
}
