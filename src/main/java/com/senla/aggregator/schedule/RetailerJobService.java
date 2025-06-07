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
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.senla.aggregator.config.batch.helper.Constants.IMPORT_CARDS_FILE_PREFIX;
import static com.senla.aggregator.schedule.helper.Constants.*;

@Service
@RequiredArgsConstructor
public class RetailerJobService {

    private final JobHelper jobHelper;
    private final JobScheduler jobScheduler;
    private final RetailerMapper retailerMapper;
    private final GmailApiService gmailApiService;
    private final RetailerRepository retailerRepository;
    private final ProductCardRepository productCardRepository;
    private final ProductCardBatchService productCardBatchService;
    private final AutoUpdateCardRepository autoUpdateCardRepository;

    @Recurring(id = "best-offers-job", cron = "${schedule.retailer-morning}")
    @Job(name = "Send daily reports about best offers among product cards to all retailers")
    public void executeBestOffersJob() {
        Stream<RetailerGetDto> retailerStream = retailerRepository.findAll()
                .stream()
                .map(retailerMapper::toRetailerGetDto);

        jobScheduler.enqueue(retailerStream, this::executeBestOffersForRetailerJob);
    }

    @Job(name = "Send daily report about best offers among product cards to retailer: %0")
    public void executeBestOffersForRetailerJob(RetailerGetDto retailer) throws IOException {
        String[] recipients = {retailer.getEmail()};
        List<BestOffer> bestOffers = productCardRepository.findBestOffers(retailer.getId());

        if (bestOffers.isEmpty()) {
            sendBestOffersNotFoundMail(retailer.getName(), recipients);
            return;
        }

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
                .recipients(recipients)
                .build();
        try {
            gmailApiService.sendEmail(emailRequest);
        } finally {
            FileUtil.deleteFileWithoutException(reportFile.getAbsolutePath());
        }
    }

    @Recurring(id = "update-cards-job", cron = "${schedule.daily-update-cards}")
    public void executeUpdateCardsJob() {
        Stream<AutoUpdateCard> infoStream = autoUpdateCardRepository.findWithRetailerBy().stream();
        jobScheduler.enqueue(infoStream, this::executeUpdateCardsForRetailerJob);
    }

    @Job(name = "Daily update product cards info: %0")
    public void executeUpdateCardsForRetailerJob(AutoUpdateCard info) {
        String downloadLink = info.getDownloadLink();
        String retailerName = info.getRetailer().getName();
        String[] recipients = {info.getRetailer().getEmail()};

        if (jobHelper.isFileTooLarge(downloadLink)) {
            sendFileValidationFailedMail(retailerName, recipients, FILE_TOO_LARGE_MESSAGE);
            return;
        }

        Optional<ContentType> optionalContentType = jobHelper.getSupportedContentType(downloadLink);
        if (optionalContentType.isEmpty()) {
            sendFileValidationFailedMail(
                    retailerName,
                    recipients,
                    CONTENT_NOT_SUPPORTED_MESSAGE
            );
            return;
        }

        File file = jobHelper.downloadFileByLink(downloadLink, IMPORT_CARDS_FILE_PREFIX);

        productCardBatchService.importProductCards(
                file,
                optionalContentType.get(),
                info.getRetailer(),
                info.getVerifiedProductsOnly()
        );
    }

    private void sendBestOffersNotFoundMail(String retailerName, String[] recipients) {
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(BEST_OFFERS_NOT_FOUND_MAIL_SUBJECT)
                .templateName(BEST_OFFERS_NOT_FOUND_TEMPLATE)
                .modelParam(RETAILER_NAME_PARAM, retailerName)
                .recipients(recipients)
                .build();

        gmailApiService.sendEmail(emailRequest);
    }

    private void sendFileValidationFailedMail(String retailerName, String[] recipients, String errorMessage) {
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(INPUT_FILE_NOT_VALID_MAIL_SUBJECT)
                .templateName(INPUT_FILE_NOT_VALID_TEMPLATE)
                .modelParam(RETAILER_NAME_PARAM, retailerName)
                .modelParam(ERROR_MESSAGE_PARAM, errorMessage)
                .recipients(recipients)
                .build();

        gmailApiService.sendEmail(emailRequest);
    }
}
