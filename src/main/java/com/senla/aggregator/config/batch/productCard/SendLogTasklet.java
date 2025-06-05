package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.config.batch.helper.Constants;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.service.mail.GmailApiService;
import com.senla.aggregator.service.minio.MinioService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Component
@RequiredArgsConstructor
public class SendLogTasklet implements Tasklet {

    private final MinioService minioService;
    private final GmailApiService gmailApiService;

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
        JobParameters jobParameters = getJobParameters(chunkContext);
        ExecutionContext executionContext = getExecutionContext(chunkContext);
        ContentType contentType = executionContext.get(OUTPUT_CONTENT_TYPE_PARAM, ContentType.class);

        String minioFileUrl = minioService.saveFileByBucketAndPath(
                executionContext.getString(OUTPUT_FILE_PATH_PARAM),
                contentType,
                executionContext.getString(OUTPUT_BUCKET_PARAM),
                executionContext.getString(OUTPUT_MINIO_PATH_PARAM)
        );

        sendEmail(minioFileUrl, jobParameters, executionContext, chunkContext);

        return RepeatStatus.FINISHED;
    }

    private void sendEmail(String minioFileUrl,
                           JobParameters jobParameters,
                           ExecutionContext executionContext,
                           ChunkContext chunkContext) {
        String[] recipients = { jobParameters.getString(CONTACT_EMAIL_PARAM) };

        EmailRequest emailRequest = EmailRequest.builder()
                .subject(executionContext.getString(EMAIL_SUBJECT_PARAM))
                .templateName(executionContext.getString(EMAIL_TEMPLATE_PARAM))
                .modelParam(JOB_NAME_PARAM, getJobName(chunkContext))
                .modelParam(RETAILER_NAME_PARAM, jobParameters.getString(RETAILER_NAME_PARAM))
                .modelParam(MINIO_FILE_URL_PARAM, minioFileUrl)
                .modelParam(JOB_STATUS_PARAM, getJobStatus(chunkContext))
                .recipients(recipients)
                .build();

        gmailApiService.sendEmail(emailRequest);
    }

    private JobParameters getJobParameters(ChunkContext chunkContext) {
        return chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getJobParameters();
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext();
    }

    private String getJobName(ChunkContext chunkContext) {
        return chunkContext.getStepContext()
                .getJobName();
    }

    private String getJobStatus(ChunkContext chunkContext) {
        return chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .getString(MAIN_STEP_STATUS_PARAM);
    }
}
