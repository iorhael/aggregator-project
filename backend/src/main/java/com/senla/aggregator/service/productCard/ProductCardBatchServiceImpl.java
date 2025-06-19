package com.senla.aggregator.service.productCard;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.AutoUpdateCardDto;
import com.senla.aggregator.dto.JobInfoDto;
import com.senla.aggregator.dto.PaginationStatsDto;
import com.senla.aggregator.mapper.AutoUpdateCardMapper;
import com.senla.aggregator.mapper.JobExecutionMapper;
import com.senla.aggregator.model.AutoUpdateCard;
import com.senla.aggregator.model.Retailer;
import com.senla.aggregator.model.RetailerJob;
import com.senla.aggregator.repository.AutoUpdateCardRepository;
import com.senla.aggregator.repository.RetailerJobRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.service.exception.ExceptionMessages;
import com.senla.aggregator.service.exception.SpringBatchException;
import com.senla.aggregator.util.PaginationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.senla.aggregator.config.batch.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.TMP_FILE_EXTENSION;

@Service
@RequiredArgsConstructor
public class ProductCardBatchServiceImpl implements ProductCardBatchService {

    private final RetailerRepository retailerRepository;
    private final AutoUpdateCardMapper autoUpdateCardMapper;
    private final RetailerJobRepository retailerJobRepository;
    private final AutoUpdateCardRepository autoUpdateCardRepository;

    private final JobExplorer jobExplorer;
    private final Job importProductCardsJob;
    private final Job updateProductCardsJob;
    private final Job exportProductCardsJob;
    private final JobLauncher asyncJobLauncher;
    private final JobExecutionMapper jobExecutionMapper;

    @Override
    public Long importProductCards(MultipartFile file,
                                   UUID retailerOwnerId,
                                   Boolean verifiedProductsOnly) throws IOException {
        ContentType contentType = ContentType.fromValue(file.getContentType());
        File tempFile = File.createTempFile(IMPORT_CARDS_FILE_PREFIX, TMP_FILE_EXTENSION);
        file.transferTo(tempFile);

        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        return importProductCards(tempFile, contentType, retailer, verifiedProductsOnly);
    }

    @Override
    public Long importProductCards(File tempFile,
                                   ContentType contentType,
                                   Retailer retailer,
                                   Boolean verifiedProductsOnly) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString(TEMP_FILE_PARAM, tempFile.getAbsolutePath())
                .addString(VERIFIED_PRODUCTS_ONLY_PARAM, Boolean.toString(verifiedProductsOnly))
                .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                .addString(RETAILER_NAME_PARAM, retailer.getName())
                .addString(CONTACT_EMAIL_PARAM, retailer.getEmail())
                .addString(CONTENT_TYPE_PARAM, contentType.getValue())
                .addLong(TIME_PARAM, System.currentTimeMillis())
                .toJobParameters();

        return launchAndPersistJob(importProductCardsJob, jobParameters, retailer);
    }

    @Override
    @Transactional
    public void enableAutoUpdate(AutoUpdateCardDto dto, UUID retailerOwnerId) {
        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        AutoUpdateCard info = autoUpdateCardMapper.toAutoUpdateCard(dto);
        info.setRetailer(retailer);

        autoUpdateCardRepository.save(info);
    }

    @Override
    @Transactional
    public void disableAutoUpdate(UUID retailerOwnerId) {
        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        autoUpdateCardRepository.deleteById(retailer.getId());
    }

    @Override
    @Transactional
    public void changeAutoUpdateRules(AutoUpdateCardDto dto, UUID retailerOwnerId) {
        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        AutoUpdateCard info = autoUpdateCardRepository.findById(retailer.getId())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.AUTO_UPDATE_INFO_NOT_FOUND));

        autoUpdateCardMapper.updateAutoUpdateCard(info, dto);
    }

    @Override
    public Long updateProductCards(MultipartFile file, UUID retailerOwnerId) throws IOException {
        ContentType contentType = ContentType.fromValue(file.getContentType());
        File tempFile = File.createTempFile(UPDATE_CARDS_FILE_PREFIX, TMP_FILE_EXTENSION);
        file.transferTo(tempFile);

        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(TEMP_FILE_PARAM, tempFile.getAbsolutePath())
                .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                .addString(RETAILER_NAME_PARAM, retailer.getName())
                .addString(CONTACT_EMAIL_PARAM, retailer.getEmail())
                .addString(CONTENT_TYPE_PARAM, contentType.getValue())
                .addLong(TIME_PARAM, System.currentTimeMillis())
                .toJobParameters();

        return launchAndPersistJob(updateProductCardsJob, jobParameters, retailer);
    }

    @Override
    public Long exportProductCards(UUID retailerOwnerId, ContentType contentType) throws IOException {
        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));
        Path tempFile = Files.createTempFile(EXPORT_CARDS_FILE_PREFIX, TMP_FILE_EXTENSION);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(TEMP_FILE_PARAM, tempFile.toAbsolutePath().toString())
                .addString(RETAILER_ID_PARAM, retailer.getId().toString())
                .addString(RETAILER_NAME_PARAM, retailer.getName())
                .addString(CONTACT_EMAIL_PARAM, retailer.getEmail())
                .addString(CONTENT_TYPE_PARAM, contentType.getValue())
                .addLong(TIME_PARAM, System.currentTimeMillis())
                .toJobParameters();

        return launchAndPersistJob(exportProductCardsJob, jobParameters, retailer);
    }

    @Override
    @Transactional
    public PaginationStatsDto<JobInfoDto> getExecutionsHistory(UUID retailerOwnerId, int pageNo, int pageSize) {
        Retailer retailer = retailerRepository.findByOwnerId(retailerOwnerId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.RETAILER_NOT_FOUND));

        Page<Long> jobExecutionsIds = retailerJobRepository.findRetailerJobExecutionIds(
                retailer.getId(),
                PageRequest.of(pageNo, pageSize)
        );

        Page<JobInfoDto> content = jobExecutionsIds
                .map(jobExplorer::getJobExecution)
                .map(jobExecutionMapper::toJobInfoDto);

        return PaginationUtil.convertToPaginationStatsDto(content);
    }

    private Long launchAndPersistJob(Job job, JobParameters jobParameters, Retailer retailer) {
        try {
            long jobExecutionId = asyncJobLauncher.run(job, jobParameters).getId();

            RetailerJob retailerJob = new RetailerJob();
            retailerJob.setJobExecutionId(jobExecutionId);
            retailerJob.setRetailer(retailer);
            retailerJobRepository.save(retailerJob);

            return retailerJob.getJobExecutionId();
        } catch (JobExecutionException e) {
            throw new SpringBatchException(ExceptionMessages.JOB_LAUNCH_FAILED, e);
        }
    }
}
