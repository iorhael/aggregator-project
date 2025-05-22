package com.senla.aggregator.schedule;

import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.model.User;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.schedule.helper.JobHelper;
import com.senla.aggregator.service.mail.GmailApiService;
import com.senla.aggregator.util.FileUtil;
import com.senla.aggregator.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static com.senla.aggregator.schedule.helper.Constants.*;
import static com.senla.aggregator.util.CommonConstants.JSON_FILE_EXTENSION;

@Service
@RequiredArgsConstructor
public class AdminJobService {

    private final JobHelper jobHelper;
    private final GmailApiService gmailApiService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Recurring(id = "unverified-products-job", cron = "${schedule.admin-morning}")
    @Job(name = "Send daily report about unverified products to admins")
    public void executeUnverifiedProductsJob() throws IOException {
        String[] adminMails = getAdminMails();

        Instant yesterday = TimeUtil.getStartOfDay(-1);
        Instant today = TimeUtil.getStartOfDay(0);
        List<Product> unverifiedProducts = productRepository.findUnverified(yesterday, today);

        if (unverifiedProducts.isEmpty()) return;

        File reportFile = jobHelper.generateReportFile(
                unverifiedProducts,
                UNVERIFIED_PRODUCTS_REPORT_NAME,
                JSON_FILE_EXTENSION
        );

        EmailRequest emailRequest = EmailRequest.builder()
                .subject(UNVERIFIED_PRODUCTS_MAIL_SUBJECT)
                .templateName(UNVERIFIED_PRODUCTS_TEMPLATE)
                .modelParam(PRODUCT_COUNT_PARAM, unverifiedProducts.size())
                .modelParam(PERIOD_START_PARAM, yesterday)
                .modelParam(PERIOD_END_PARAM, today)
                .attachment(reportFile)
                .recipients(adminMails)
                .build();
        try {
            gmailApiService.sendEmail(emailRequest);
        } finally {
            FileUtil.deleteFileWithoutException(reportFile.getAbsolutePath());
        }
    }

    private String[] getAdminMails() {
        return userRepository.findAllByRole(Role.ADMIN)
                .stream()
                .map(User::getEmail)
                .toArray(String[]::new);
    }
}
