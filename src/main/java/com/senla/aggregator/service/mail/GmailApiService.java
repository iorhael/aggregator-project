package com.senla.aggregator.service.mail;

import com.senla.aggregator.dto.mail.EmailRequest;

public interface GmailApiService {
    void sendEmail(EmailRequest request);
}
