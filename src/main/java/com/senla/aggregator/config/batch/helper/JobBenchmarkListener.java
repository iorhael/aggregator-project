package com.senla.aggregator.config.batch.helper;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j(topic = "batchLogger")
public class JobBenchmarkListener implements JobExecutionListener {
    private static final Logger consoleLog = org.slf4j.LoggerFactory.getLogger(JobBenchmarkListener.class);

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String file = String.format("import-cards-log-%s-%s", jobExecution.getJobInstance().getInstanceId(), timestamp);
        MDC.put("batchLogFile", file);
        log.info("===== Import product cards log {} =====", timestamp);
    }

    @Override
    public void afterJob(@NotNull JobExecution jobExecution) {
        consoleLog.info("******* Job completed in {} ms *******", System.currentTimeMillis() - startTime);
//        if (jobExecution.getStatus() == BatchStatus.FAILED) {
//            log.info("!!!! Import job failed due to one or more critical errors !!!!");
//            jobExecution.getAllFailureExceptions()
//                    .forEach(e -> log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage()));
//        } else {
//            log.info("***** Job completed successfully *****");
//        }
//        log.info("~~~~~ Job execution time: {} ms ~~~~~", System.currentTimeMillis() - startTime);
        MDC.clear();
    }
}
