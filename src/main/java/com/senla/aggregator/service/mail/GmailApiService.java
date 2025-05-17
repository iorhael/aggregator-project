package com.senla.aggregator.service.mail;

import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface GmailApiService {
    CompletableFuture<Boolean> sendEmail(String subject, String body, MultipartFile attachment, String ... recipients);
}
