package com.senla.aggregator.service.mail;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.senla.aggregator.model.credential.GoogleCredentials;
import com.senla.aggregator.util.CommonConstants;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailApiServiceImpl implements GmailApiService {
    private static final String SENT_LABEL = "SENT";
    private static final String TEXT_FORMAT = "text/plain";

    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final GoogleCredentials googleCredentials;
    private final GoogleCredentialManager credentialManager;

    @Async
    @Override
    public CompletableFuture<Boolean> sendEmail(String subject,
                                                String body,
                                                MultipartFile attachment,
                                                String ... recipients) {
        try {
            Message message = createMessageWithEmail(
                    createEmail(
                            String.join(CommonConstants.COMMA, recipients),
                            googleCredentials.fromEmail(),
                            subject,
                            body,
                            attachment)
            );

            Boolean isSent = new Gmail.Builder(httpTransport, jsonFactory, credentialManager.getCredential())
                    .build()
                    .users()
                    .messages()
                    .send(googleCredentials.fromEmail(), message)
                    .execute()
                    .getLabelIds()
                    .contains(SENT_LABEL);

            return CompletableFuture.completedFuture(isSent);
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage(), e);
            return CompletableFuture.completedFuture(false);
        }
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return new Message().setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
    }

    private MimeMessage createEmail(
            String to,
            String from,
            String subject,
            String bodyText,
            MultipartFile attachment) throws MessagingException, IOException {

        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        email.setFrom(new InternetAddress(from));
        email.addRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(to));
        email.setSubject(subject);
        email.setText(bodyText);
        addAttachmentToEmail(email, bodyText, attachment);

        return email;

    }

    private void addAttachmentToEmail(MimeMessage email,
                                      String bodyText,
                                      MultipartFile attachment) throws MessagingException, IOException {
        if (Objects.isNull(attachment) || attachment.isEmpty()) return;

        Multipart multipart = new MimeMultipart();
        MimeBodyPart testPart = new MimeBodyPart();
        testPart.setContent(bodyText, TEXT_FORMAT);
        multipart.addBodyPart(testPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource ds = new ByteArrayDataSource(attachment.getBytes(), attachment.getContentType());
        attachmentPart.setDataHandler(new DataHandler(ds));
        attachmentPart.setFileName(attachment.getOriginalFilename());
        multipart.addBodyPart(attachmentPart);

        email.setContent(multipart);
    }
}
