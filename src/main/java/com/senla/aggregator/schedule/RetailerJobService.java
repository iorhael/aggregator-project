package com.senla.aggregator.schedule;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.dto.productCard.BestOffer;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.mapper.RetailerMapper;
import com.senla.aggregator.model.AutoUpdateCard;
import com.senla.aggregator.repository.AutoUpdateCardRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.repository.productCard.ProductCardRepository;
import com.senla.aggregator.schedule.helper.JobHelper;
import com.senla.aggregator.service.mail.GmailApiService;
import com.senla.aggregator.service.productCard.ProductCardBatchService;
import com.senla.aggregator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.senla.aggregator.config.batch.helper.Constants.IMPORT_CARDS_FILE_PREFIX;
import static com.senla.aggregator.schedule.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.TMP_FILE_EXTENSION;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetailerJobService {

    private final JobHelper jobHelper;
    private final JobScheduler jobScheduler;
    private final RestTemplate restTemplate;
    private final RetailerMapper retailerMapper;
    private final GmailApiService gmailApiService;
    private final RetailerRepository retailerRepository;
    private final ProductCardRepository productCardRepository;
    private final ProductCardBatchService productCardBatchService;
    private final AutoUpdateCardRepository autoUpdateCardRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    @Recurring(id = "best-offers-job", cron = "${schedule.retailer-morning}")
    @Job(name = "Send daily reports about best offers among product cards to all retailers")
    public void executeBestOffersJob() {
        retailerRepository.findAll()
                .stream()
                .map(retailerMapper::toRetailerGetDto)
                .forEach(retailer ->
                        jobScheduler.enqueue(() -> executeBestOffersForRetailerJob(retailer)));
    }

    @Job(name = "Send daily report about best offers among product cards to retailer: %0")
    public void executeBestOffersForRetailerJob(RetailerGetDto retailer) throws IOException {
        String[] mails = {retailer.getEmail()};
        List<BestOffer> bestOffers = productCardRepository.findBestOffers(retailer.getId());

        if (bestOffers.isEmpty()) sendBestOffersNotFoundMail(retailer.getName(), mails);

        File reportFile = jobHelper.generateReportFile(
                bestOffers,
                BEST_OFFERS_REPORT_NAME,
                ContentType.JSON.getFileExtension()
        );

        EmailRequest emailRequest = EmailRequest.builder()
                .subject(BEST_OFFERS_SUCCESS_MAIL_SUBJECT)
                .templateName(BEST_OFFERS_SUCCESS_TEMPLATE_NAME)
                .modelParam(RETAILER_NAME_PARAM, retailer.getName())
                .modelParam(OFFERS_COUNT_PARAM, bestOffers.size())
                .attachment(reportFile)
                .recipients(mails)
                .build();
        try {
            gmailApiService.sendEmail(emailRequest);
        } finally {
            FileUtil.deleteFileWithoutException(reportFile.getAbsolutePath());
        }
    }

    @Recurring(id = "update-cards-job", cron = "${schedule.daily-update-cards}")
    public void executeUpdateCardsJob() {
        autoUpdateCardRepository.findWithRetailerBy()
                .forEach(info ->
                        jobScheduler.enqueue(() -> executeUpdateCardsForRetailerJob(info)));
    }

    @Job(name = "Daily update product cards info: %0")
    public void executeUpdateCardsForRetailerJob(AutoUpdateCard info) throws IOException {
        byte[] fileBytes = fetchFileBytes(info.getDownloadLink());

        if (fileBytes.length > maxFileSize.toBytes()) {
            log.error("Downloaded file is too large: {}", info);
            return;
        }

        Path tempFilePath = Files.createTempFile(IMPORT_CARDS_FILE_PREFIX, TMP_FILE_EXTENSION);
        Files.write(tempFilePath, fileBytes);

        Optional<ContentType> optionalContentType = jobHelper.getContentTypeOfFile(tempFilePath);
        if (optionalContentType.isEmpty()) return;

        productCardBatchService.importProductCards(
                tempFilePath.toFile(),
                optionalContentType.get(),
                info.getRetailer(),
                info.getVerifiedProductsOnly()
        );
    }

    private void sendBestOffersNotFoundMail(String retailerName, String[] mails) {
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(BEST_OFFERS_NOT_FOUND_MAIL_SUBJECT)
                .templateName(BEST_OFFERS_NOT_FOUND_TEMPLATE)
                .modelParam(RETAILER_NAME_PARAM, retailerName)
                .recipients(mails)
                .build();

        gmailApiService.sendEmail(emailRequest);
    }

    private byte[] fetchFileBytes(String downloadLink) throws IOException {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(downloadLink, byte[].class);

        if (!response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            throw new IOException("Failed to download file. Status: " + response.getStatusCode());
        }

        return response.getBody();
    }
}
