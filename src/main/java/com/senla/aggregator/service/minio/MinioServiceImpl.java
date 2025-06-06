package com.senla.aggregator.service.minio;

import com.senla.aggregator.controller.helper.ContentType;
import com.senla.aggregator.service.exception.MinioException;
import com.senla.aggregator.util.MinioUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.senla.aggregator.util.CommonConstants.MD5_ALGORITHM;
import static com.senla.aggregator.util.CommonConstants.SLASH;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private static final String IMAGE_BUCKET = "images";
    private static final String PRODUCTS_DIRECTORY = "products";

    private final MinioClient minioClient;
    private final MinioClient signMinioClient;

    private String imageBucketUrl;

    @Value("${minio.external-server-url}")
    private String externalServerUrl;

    @Value("${minio.default-link-expiration}")
    private int linkExpiration;

    @PostConstruct
    public void init() {
        imageBucketUrl = externalServerUrl + SLASH + IMAGE_BUCKET;
    }

    @Override
    public String saveProductImage(InputStream stream, ContentType contentType) {
        try {
            byte[] imageBytes = stream.readAllBytes();
            String checksum = getContentChecksum(imageBytes);
            String objectPath = PRODUCTS_DIRECTORY + SLASH + checksum;

            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(IMAGE_BUCKET)
                        .object(objectPath)
                        .stream(bis, bis.available(), -1)
                        .contentType(contentType.getValue())
                        .build());
            }

            return imageBucketUrl + SLASH + objectPath;
        } catch (IOException | GeneralSecurityException | io.minio.errors.MinioException e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    @Override
    public String updateProductImage(String oldImageLink, InputStream stream, ContentType contentType) {
        String newImageLink = saveProductImage(stream, contentType);
        if (!oldImageLink.equals(newImageLink)) deleteProductImage(oldImageLink);

        return newImageLink;
    }

    @Override
    public void deleteProductImage(String imageLink) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(IMAGE_BUCKET)
                    .object(MinioUtil.getObjectPath(imageLink, imageBucketUrl))
                    .build());
        } catch (IOException | GeneralSecurityException | io.minio.errors.MinioException e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    @Override
    public String saveFileByBucketAndPath(String filePath,
                                          ContentType contentType,
                                          String bucket,
                                          String objectPath) {
        try {
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectPath)
                    .filename(filePath)
                    .contentType(contentType.getValue())
                    .build());

            return signMinioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(objectPath)
                    .expiry(linkExpiration)
                    .build());
        } catch (IOException | GeneralSecurityException | io.minio.errors.MinioException e) {
            throw new MinioException(e.getMessage(), e);
        }
    }

    private String getContentChecksum(byte[] bytes) throws NoSuchAlgorithmException {
        byte[] md5Bytes = MessageDigest.getInstance(MD5_ALGORITHM).digest(bytes);
        return Base64.getEncoder().encodeToString(md5Bytes);
    }
}
