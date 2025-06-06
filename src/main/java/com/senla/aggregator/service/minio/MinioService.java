package com.senla.aggregator.service.minio;

import com.senla.aggregator.controller.helper.ContentType;

import java.io.InputStream;

public interface MinioService {

    String saveProductImage(InputStream stream, ContentType contentType);

    String updateProductImage(String oldImageLink, InputStream stream, ContentType contentType);

    void deleteProductImage(String imageLink);

    String saveFileByBucketAndPath(String filePath, ContentType contentType, String bucket, String objectPath);
}
