package com.senla.aggregator.config.batch.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.service.mail.GmailApiService;
import com.senla.aggregator.service.minio.MinioService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class SendLogTasklet implements Tasklet {

    private final MinioService minioService;
    private final GmailApiService gmailApiService;

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) {
        String logFileName = MDC.get("batchLogFile") + ".log";
        String logPath = Paths.get("logs", "batch", logFileName).toAbsolutePath().toString();

        String minioFileUrl = minioService.saveFileByBucketAndPath(
                logPath,
                ContentType.PLAIN_TEXT,
                "logs", "import-cards/" + logFileName
        );

        sendEmail(minioFileUrl, chunkContext);

        return RepeatStatus.FINISHED;
    }

    private void sendEmail(String minioFileUrl, ChunkContext chunkContext) {
        String[] recipients = { getContactEmail(chunkContext) };

        EmailRequest emailRequest = EmailRequest.builder()
                .subject("Job Execution Report")
                .templateName("import-product-cards")
                .modelParam("retailerName", getRetailerName(chunkContext))
                .modelParam("minioFileUrl", minioFileUrl)
                .modelParam("jobStatus", getMainStepStatus(chunkContext))
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

    private String getContactEmail(ChunkContext chunkContext) {
        return getJobParameters(chunkContext)
                .getString("contactEmail");
    }

    private String getRetailerName(ChunkContext chunkContext) {
        return getJobParameters(chunkContext)
                .getString("retailerName");
    }

    private String getMainStepStatus(ChunkContext chunkContext) {
        return chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .getString("mainStepStatus");
    }
}
