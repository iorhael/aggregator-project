package com.senla.aggregator.config.batch.productCard.importJob;

import com.senla.aggregator.config.batch.helper.Constants;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.util.CommonConstants;
import org.slf4j.MDC;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

import static com.senla.aggregator.config.batch.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.*;

@Component
public class ImportJobPrepareArgsForSendStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getExecutionContext();

        String logFileName = MDC.get(LOG_FILE_PARAM) + LOG_FILE_EXTENSION;
        String logPath = Paths.get(LOG_DIRECTORY, BATCH_DIRECTORY, logFileName).toAbsolutePath().toString();

        executionContext.put(OUTPUT_FILE_PATH_PARAM, logPath);
        executionContext.put(OUTPUT_CONTENT_TYPE_PARAM, ContentType.PLAIN_TEXT);
        executionContext.put(OUTPUT_BUCKET_PARAM, IMPORT_CARDS_BUCKET_NAME);
        executionContext.put(OUTPUT_MINIO_PATH_PARAM, IMPORT_CARDS_BUCKET_DIRECTORY_NAME + SLASH + logPath);
        executionContext.put(EMAIL_SUBJECT_PARAM, IMPORT_CARDS_EMAIL_SUBJECT);
        executionContext.put(EMAIL_TEMPLATE_PARAM, CARD_BATCH_EMAIL_TEMPLATE);
    }
}
