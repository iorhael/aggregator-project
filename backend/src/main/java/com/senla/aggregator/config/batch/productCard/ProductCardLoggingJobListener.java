package com.senla.aggregator.config.batch.productCard;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Component
@Slf4j(topic = "batchLogger")
public class ProductCardLoggingJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT));
        String file = String.format(CARDS_LOG_FILE_TEMPLATE, jobExecution.getJobInstance().getInstanceId(), timestamp);
        MDC.put(LOG_FILE_PARAM, file);

        log.info("===== Product card job log {} =====", timestamp);
    }

    @Override
    public void afterJob(@NotNull JobExecution jobExecution) {
        MDC.clear();
    }
}
