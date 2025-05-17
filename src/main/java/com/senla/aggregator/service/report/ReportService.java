package com.senla.aggregator.service.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.aggregator.model.Product;
import com.senla.aggregator.model.Role;
import com.senla.aggregator.model.User;
import com.senla.aggregator.repository.ProductRepository;
import com.senla.aggregator.repository.UserRepository;
import com.senla.aggregator.service.mail.GmailApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ObjectMapper objectMapper;
    private final GmailApiService gmailApiService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Recurring(id = "not-verified-products-report", cron = "${schedule.every-morning}")
    @Job(name = "Send report about unverified products to admins")
    public void generateUnverifiedProductsReport() throws IOException {
        String[] adminMails = userRepository.findAllByRole(Role.ADMIN)
                .stream()
                .map(User::getEmail)
                .toArray(String[]::new);

        Instant today = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        Instant yesterday = LocalDate.now()
                .minusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        List<Product> unverifiedProducts = productRepository.findUnverified(yesterday, today);

        File reportFile = File.createTempFile("unverified-report-", ".json");
        try {
            objectMapper.writeValue(reportFile, unverifiedProducts);
            gmailApiService.sendEmail("Admins Report", String.format("Yesterday created %d products", unverifiedProducts.size()), reportFile, adminMails);
        } finally {
            deleteFileWithoutException(reportFile.getAbsolutePath());
        }
    }

    private void deleteFileWithoutException(String path) {
       try {
           Files.deleteIfExists(Path.of(path));
       } catch (IOException e) {
          log.error(e.getMessage(), e);
       }
    }
}
