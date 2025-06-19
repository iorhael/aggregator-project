package com.senla.aggregator.service.minio;

import com.senla.aggregator.controller.helper.ContentType;

import java.io.InputStream;

public interface MinioService {

    String saveImage(InputStream stream, String imageDirectory, ContentType contentType);

    String updateImage(String oldImageLink, String imageDirectory, InputStream stream, ContentType contentType);

    String copyImageFromTemp(String source);

    void deleteImage(String imageLink);

    String saveFileByBucketAndPath(String filePath, ContentType contentType, String bucket, String objectPath);
}
