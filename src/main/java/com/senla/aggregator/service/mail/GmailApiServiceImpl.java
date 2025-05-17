package com.senla.aggregator.service.mail;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.senla.aggregator.model.credential.GoogleCredentials;
import com.senla.aggregator.service.exception.GmailException;
import com.senla.aggregator.util.CommonConstants;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static com.senla.aggregator.service.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class GmailApiServiceImpl implements GmailApiService {
    private static final String SENT_LABEL = "SENT";
    private static final String TEXT_FORMAT = "text/plain";

    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final GoogleCredentials googleCredentials;
    private final GoogleCredentialManager credentialManager;

    @Override
    public void sendEmail(String subject, String body, String... recipients) {
        sendEmail(subject, body, null, recipients);
    }

    @Override
    public void sendEmail(String subject,
                          String body,
                          File attachment,
                          String... recipients) {
        try {
            Message message = createMessageWithEmail(
                    createEmail(
                            String.join(CommonConstants.COMMA, recipients),
                            googleCredentials.fromEmail(),
                            subject,
                            body,
                            attachment)
            );

            boolean wasSent = new Gmail.Builder(httpTransport, jsonFactory, credentialManager.getCredential())
                    .build()
                    .users()
                    .messages()
                    .send(googleCredentials.fromEmail(), message)
                    .execute()
                    .getLabelIds()
                    .contains(SENT_LABEL);

            if (!wasSent) throw new GmailException(GMAIL_NOT_SENT);
        } catch (MessagingException | IOException e) {
            throw new GmailException(GMAIL_NOT_SENT, e);
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
            File attachment) throws MessagingException {
        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        email.setFrom(new InternetAddress(from));
        email.addRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(to));
        email.setSubject(subject);

        if (Objects.nonNull(attachment) && attachment.exists()) {
            email.setContent(createContentWithAttachment(bodyText, attachment));
        } else {
            email.setText(bodyText);
        }

        return email;
    }

    private Multipart createContentWithAttachment(String bodyText,
                                                  File attachment) throws MessagingException {
        Multipart multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(bodyText, TEXT_FORMAT);
        multipart.addBodyPart(textPart);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource ds = new FileDataSource(attachment);
        attachmentPart.setDataHandler(new DataHandler(ds));
        attachmentPart.setFileName(attachment.getName());
        multipart.addBodyPart(attachmentPart);

        return multipart;
    }
}
