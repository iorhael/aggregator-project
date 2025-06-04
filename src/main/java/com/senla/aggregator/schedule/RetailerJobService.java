package com.senla.aggregator.schedule;

import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.dto.productCard.BestOffer;
import com.senla.aggregator.dto.retailer.RetailerGetDto;
import com.senla.aggregator.mapper.RetailerMapper;
import com.senla.aggregator.repository.productCard.ProductCardRepository;
import com.senla.aggregator.repository.RetailerRepository;
import com.senla.aggregator.schedule.helper.JobHelper;
import com.senla.aggregator.service.mail.GmailApiService;
import com.senla.aggregator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.senla.aggregator.schedule.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.JSON_FILE_EXTENSION;

@Service
@RequiredArgsConstructor
public class RetailerJobService {

    private final JobHelper jobHelper;
    private final JobScheduler jobScheduler;
    private final RetailerMapper retailerMapper;
    private final GmailApiService gmailApiService;
    private final RetailerRepository retailerRepository;
    private final ProductCardRepository productCardRepository;

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
                JSON_FILE_EXTENSION
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

    private void sendBestOffersNotFoundMail(String retailerName, String[] mails) {
        EmailRequest emailRequest = EmailRequest.builder()
                .subject(BEST_OFFERS_NOT_FOUND_MAIL_SUBJECT)
                .templateName(BEST_OFFERS_NOT_FOUND_TEMPLATE)
                .modelParam(RETAILER_NAME_PARAM, retailerName)
                .recipients(mails)
                .build();

        gmailApiService.sendEmail(emailRequest);
    }
}
