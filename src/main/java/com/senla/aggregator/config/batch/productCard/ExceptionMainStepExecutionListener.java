package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.config.batch.helper.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Component
@Slf4j(topic = "batchLogger")
public class ExceptionMainStepExecutionListener implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        if (stepExecution.getStatus() == BatchStatus.FAILED) {
            log.info("!!!! Job failed due to one or more critical errors !!!!");
            stepExecution.getFailureExceptions()
                    .forEach(e -> log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage()));
        } else {
            log.info("~~~~~ Job successfully finished ~~~~~");
        }

        executionContext.put(MAIN_STEP_STATUS_PARAM, stepExecution.getExitStatus().getExitCode());

        return stepExecution.getExitStatus();
    }
}
