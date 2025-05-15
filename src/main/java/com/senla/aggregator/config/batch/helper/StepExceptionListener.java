package com.senla.aggregator.config.batch.helper;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

// Делать подробные отчёты вместо этого и загружать их в minio.
@Component
public class StepExceptionListener implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> exceptions = stepExecution.getFailureExceptions();

        for (Throwable exception : exceptions) {
            switch (exception) {
                case ItemStreamException ignored -> {
                    return new ExitStatus(BatchExitStatus.BAD_INPUT.toString());
                }
                case DataIntegrityViolationException ignored -> {
                    return new ExitStatus(BatchExitStatus.DATA_INTEGRITY_VIOLATION.toString());
                }
                default -> {
                    return stepExecution.getExitStatus();
                }
            }
        }

        return stepExecution.getExitStatus();
    }
}
