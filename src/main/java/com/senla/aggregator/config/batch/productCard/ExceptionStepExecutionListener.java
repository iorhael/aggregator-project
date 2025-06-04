package com.senla.aggregator.config.batch.productCard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "batchLogger")
public class ExceptionStepExecutionListener implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        if (stepExecution.getStatus() == BatchStatus.FAILED) {
            log.info("!!!! Import job failed due to one or more critical errors !!!!");
            stepExecution.getFailureExceptions()
                    .forEach(e -> log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage()));
        } else {
            log.info("~~~~~ Import job successfully finished ~~~~~");
        }

        executionContext.put("mainStepStatus", stepExecution.getExitStatus().getExitCode());

        return stepExecution.getExitStatus();
    }
}
