package com.senla.aggregator.config.batch.productCard;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Slf4j
@Component
public class FileCleanerJobExecutionListener implements JobExecutionListener {

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        String path = jobExecution.getJobParameters().getString(IMPORT_FILE_PARAM);

        try {
            Files.deleteIfExists(Paths.get(Objects.requireNonNull(path)));
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }
}
