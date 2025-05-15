package com.senla.aggregator.service.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.JobInfoDto;
import com.senla.aggregator.mapper.JobExecutionMapper;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.model.RetailerJob;
import com.senla.aggregator.repository.RetailerJobRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.service.exception.ExceptionMessages;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.senla.aggregator.config.batch.helper.Constants.*;

@Service
@RequiredArgsConstructor
public class ProductCardBatchServiceImpl implements ProductCardBatchService {

    private final RetailerRepository retailerRepository;
    private final RetailerJobRepository retailerJobRepository;

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final Job importProductCardsJob;
    private final Job updateProductCardsJob;
    private final Job exportProductCardsJob;

    private final JobExecutionMapper jobExecutionMapper;

    @Override
    public Long importProductCards(MultipartFile file, UUID retailerOwnerId, Boolean verifiedProductsOnly) throws Exception {
        ContentType contentType = ContentType.fromValue(file.getContentType());
        File tempFile = File.createTempFile(IMPORT_CARDS_FILE_PREFIX, TMP_FILE_SUFFIX);
        file.transferTo(tempFile);

        Retailer retailer = retailerRepository.findRetailerByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(IMPORT_FILE_PARAM, tempFile.getAbsolutePath())
                .addString(VERIFIED_PRODUCTS_ONLY_PARAM, Boolean.toString(verifiedProductsOnly))
                .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                .addString(CONTENT_TYPE_PARAM, contentType.getValue())
                .addLong(TIME_PARAM, System.currentTimeMillis())
                .toJobParameters();

        return launchAndPersistJob(importProductCardsJob, jobParameters, retailer);
    }

    @Override
    public Long updateProductCards(MultipartFile file, UUID retailerOwnerId) throws Exception {
        ContentType contentType = ContentType.fromValue(file.getContentType());
        File tempFile = File.createTempFile(UPDATE_CARDS_FILE_PREFIX, TMP_FILE_SUFFIX);
        file.transferTo(tempFile);

        Retailer retailer = retailerRepository.findRetailerByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(IMPORT_FILE_PARAM, tempFile.getAbsolutePath())
                .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                .addString(CONTENT_TYPE_PARAM, contentType.getValue())
                .addLong(TIME_PARAM, System.currentTimeMillis())
                .toJobParameters();

        return launchAndPersistJob(updateProductCardsJob, jobParameters, retailer);
    }

    public Path exportProductCards(UUID retailerOwnerId, ContentType contentType) throws Exception {
        Retailer retailer = retailerRepository.findRetailerByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));
        try {
            // 1. Генерируем временный файл
            Path tempFile = Files.createTempFile("export_", "tmp");

            // 2. Подготавливаем параметры джобы
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                    .addString(CONTENT_TYPE_PARAM, contentType.name())
                    .addString(EXPORT_FILE_PARAM, tempFile.toAbsolutePath().toString())
                    .addLong(TIME_PARAM, System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(exportProductCardsJob, jobParameters);

            // 4. Ожидаем завершения (синхронно)
            while (jobExecution.isRunning()) {
                Thread.sleep(200);
            }

            // 5. Проверяем статус
            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                return tempFile;
            } else {
                throw new IllegalStateException("Export job failed with status: ");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to export products", e);
        }
    }

    @Override
    @Transactional
    public List<JobInfoDto> getExecutionsHistory(UUID retailerOwnerId, int pageNo, int pageSize) {
        Retailer retailer = retailerRepository.findRetailerByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        List<Long> jobExecutionsIds = retailerJobRepository.findRetailerJobExecutionIds(
                retailer.getId(),
                PageRequest.of(pageNo, pageSize)
        );

        return jobExecutionsIds.stream()
                .map(jobExplorer::getJobExecution)
                .map(jobExecutionMapper::toJobInfoDto)
                .toList();
    }

    private Long launchAndPersistJob(Job job, JobParameters jobParameters, Retailer retailer) throws Exception {
        Long jobExecutionId = jobLauncher.run(job, jobParameters).getId();

        RetailerJob retailerJob = new RetailerJob();
        retailerJob.setJobExecutionId(jobExecutionId);
        retailerJob.setRetailer(retailer);
        retailerJobRepository.save(retailerJob);

        return retailerJob.getJobExecutionId();
    }
}
