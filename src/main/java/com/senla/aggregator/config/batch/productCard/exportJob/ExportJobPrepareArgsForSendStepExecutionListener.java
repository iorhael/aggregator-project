package com.senla.aggregator.config.batch.productCard.exportJob;

import com.senla.aggregator.controller.helper.ContentType;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.senla.aggregator.config.batch.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.SLASH;

@Component
public class ExportJobPrepareArgsForSendStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        JobParameters jobParameters = stepExecution.getJobParameters();

        ContentType outputContentType = ContentType.fromValue(jobParameters.getString(CONTENT_TYPE_PARAM));
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT));
        String minioFileName = String.format(EXPORT_CARDS_FILE_TEMPLATE, timestamp, outputContentType.getFileExtension());

        executionContext.put(OUTPUT_FILE_PATH_PARAM, jobParameters.getString(TEMP_FILE_PARAM));
        executionContext.put(OUTPUT_CONTENT_TYPE_PARAM, outputContentType);
        executionContext.put(OUTPUT_BUCKET_PARAM, EXPORT_CARDS_BUCKET_NAME);
        executionContext.put(OUTPUT_MINIO_PATH_PARAM, EXPORT_CARDS_BUCKET_DIRECTORY_NAME + SLASH + minioFileName);
        executionContext.put(EMAIL_SUBJECT_PARAM, EXPORT_CARDS_EMAIL_SUBJECT);
        executionContext.put(EMAIL_TEMPLATE_PARAM, CARD_BATCH_EMAIL_TEMPLATE);
    }
}
