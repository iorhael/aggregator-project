package com.senla.aggregator.service.mail;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.dto.mail.EmailRequest;
import com.senla.aggregator.model.credential.GoogleCredentials;
import com.senla.aggregator.service.exception.GmailException;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static com.senla.aggregator.service.exception.ExceptionMessages.GMAIL_NOT_SENT;
import static com.senla.aggregator.util.CommonConstants.COMMA;

@Service
@RequiredArgsConstructor
public class GmailApiServiceImpl implements GmailApiService {
    private static final String SENT_LABEL = "SENT";
    private static final String LOGO_FILE_NAME = "logo.png";
    private static final String HTML_CONTENT_TYPE = "text/html; charset=utf-8";
    private static final String RELATED_MULTIPART_SUBTYPE = "related";


    private final JsonFactory jsonFactory;
    private final HttpTransport httpTransport;
    private final GoogleCredentials googleCredentials;
    private final SpringTemplateEngine templateEngine;
    private final GoogleCredentialManager credentialManager;

    @Value("classpath:static/logo.png")
    private Resource logoImage;

    @Override
    public void sendEmail(EmailRequest request) {
        try {
            MimeMessage mimeMessage = createMimeMessage(request);
            Message message = convertMimeMessageToEmail(mimeMessage);

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

    private MimeMessage createMimeMessage(EmailRequest request) throws MessagingException, IOException {
        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties()));
        email.setFrom(new InternetAddress(googleCredentials.fromEmail()));
        email.addRecipients(
                jakarta.mail.Message.RecipientType.TO,
                InternetAddress.parse(String.join(COMMA, request.getRecipients()))
        );
        email.setSubject(request.getSubject());
        email.setContent(createMultipartContent(request));

        return email;
    }

    private Multipart createMultipartContent(EmailRequest request) throws MessagingException, IOException {
        MimeMultipart multipart = new MimeMultipart();

        if (Objects.nonNull(request.getTemplateName())) {
            multipart.setSubType(RELATED_MULTIPART_SUBTYPE);
            attachHtmlPart(request.getTemplateName(), request.getModel(), multipart);
            attachImagePart(logoImage, multipart);
        }
        if (Objects.nonNull(request.getBody()))
            attachTextPart(request.getBody(), multipart);
        if (Objects.nonNull(request.getAttachment()))
            attachFilePart(request.getAttachment(), multipart);

        return multipart;
    }

    private void attachTextPart(String text, Multipart multipart) throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text);
        multipart.addBodyPart(textPart);
    }

    private void attachHtmlPart(String templateName, Map<String, Object> model, Multipart multipart)
            throws MessagingException {
        MimeBodyPart htmlPart = new MimeBodyPart();
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(model);
        String html = templateEngine.process(templateName, thymeleafContext);
        htmlPart.setContent(html, HTML_CONTENT_TYPE);
        multipart.addBodyPart(htmlPart);
    }

    private void attachFilePart(File attachment, Multipart multipart) throws MessagingException, IOException {
        MimeBodyPart filePart = new MimeBodyPart();
        filePart.attachFile(attachment);
        multipart.addBodyPart(filePart);
    }

    private void attachImagePart(Resource image, Multipart multipart) throws MessagingException, IOException {
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(image.getInputStream(), ContentType.PNG.getValue());
        imagePart.setDataHandler(new DataHandler(source));
        imagePart.setFileName(LOGO_FILE_NAME);
        imagePart.setDisposition(Part.INLINE);
        imagePart.setContentID(LOGO_FILE_NAME);
        multipart.addBodyPart(imagePart);
    }

    private Message convertMimeMessageToEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return new Message().setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
    }
}
