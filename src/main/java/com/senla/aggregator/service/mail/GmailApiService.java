package com.senla.aggregator.service.mail;

import java.io.File;

public interface GmailApiService {
    void sendEmail(String subject, String body, String... recipients);

    void sendEmail(String subject, String body, File attachment, String... recipients);
}
