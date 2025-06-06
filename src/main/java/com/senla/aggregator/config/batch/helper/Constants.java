package com.senla.aggregator.config.batch.helper;

import com.senla.aggregator.controller.helper.ContentType;

public final class Constants {
    public static final String IMPORT_CARDS_JOB_NAME = "importProductCardsJob";
    public static final String UPDATE_CARDS_JOB_NAME = "updateProductCardsJob";
    public static final String EXPORT_CARDS_JOB_NAME = "exportProductCardsJob";
    public static final String JOB_NAME_PARAM = "jobName";
    public static final String JOB_STATUS_PARAM = "jobStatus";
    public static final String MAIN_STEP_NAME = "mainStep";
    public static final String SEND_LOG_STEP_NAME = "sendLogStep";
    public static final String MAIN_STEP_STATUS_PARAM = "mainStepStatus";
    public static final String MINIO_FILE_URL_PARAM = "minioFileUrl";
    public static final String VERIFIED_PRODUCTS_ONLY_PARAM = "verifiedProductsOnly";
    public static final String RETAILER_ID_PARAM = "retailerId";
    public static final String RETAILER_NAME_PARAM = "retailerName";
    public static final String CONTACT_EMAIL_PARAM = "contactEmail";
    public static final String CONTENT_TYPE_PARAM = "contentType";
    public static final String TIME_PARAM = "time";
    public static final String TEMP_FILE_PARAM = "tempFile";

    public static final String JSON_IMPORT_READER_NAME = "jsonImportReader";
    public static final String XML_IMPORT_READER_NAME = "xmlImportReader";
    public static final String CSV_IMPORT_READER_NAME = "csvImportReader";

    public static final String JSON_EXPORT_WRITER_NAME = "jsonImportWriter";
    public static final String XML_EXPORT_WRITER_NAME = "xmlImportWriter";
    public static final String CSV_EXPORT_WRITER_NAME = "csvImportWriter";

    public static final String PRODUCT_CARD_NAME = "productCard";
    public static final String PRODUCT_CARDS_NAME = "productCards";
    public static final String LOG_FILE_PARAM = "batchLogFile";
    public static final String CARDS_LOG_FILE_TEMPLATE = "cards-job-log-%s-%s";
    public static final String INVALID_CONTENT_TYPE = "Invalid content type: %s";
    public static final String LOG_DIRECTORY = "logs";
    public static final String BATCH_DIRECTORY = "batch";
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
    public static final String OUTPUT_FILE_PATH_PARAM = "outputFilePath";
    public static final String OUTPUT_CONTENT_TYPE_PARAM = "outputContentType";
    public static final String OUTPUT_BUCKET_PARAM = "outputBucket";
    public static final String OUTPUT_MINIO_PATH_PARAM = "outputMinioPath";
    public static final String EMAIL_SUBJECT_PARAM = "emailSubject";
    public static final String EMAIL_TEMPLATE_PARAM = "emailTemplateName";

    public static final String[] PRODUCT_CARD_FIELDS_FILE = {"product_name", "description", "warranty", "installment_period", "max_delivery_time", "price"};
    public static final String[] PRODUCT_CARD_FIELDS_MODEL = {"productName", "description", "warranty", "installmentPeriod", "maxDeliveryTime", "price"};
    public static final ContentType[] SUPPORTED_CONTENT_TYPES = {ContentType.JSON, ContentType.XML, ContentType.CSV};

    public static final String CARD_BATCH_EMAIL_TEMPLATE = "card-batch-job";

    public static final String EXPORT_CARDS_FILE_PREFIX = "export-cards-";
    public static final String EXPORT_CARDS_FILE_TEMPLATE = "export-cards-%s.%s";
    public static final String EXPORT_CARDS_BUCKET_NAME = "export";
    public static final String EXPORT_CARDS_BUCKET_DIRECTORY_NAME = "product-cards";
    public static final String EXPORT_CARDS_EMAIL_SUBJECT = "Cards Export Job Report";

    public static final String IMPORT_CARDS_FILE_PREFIX = "import-cards-";
    public static final String IMPORT_CARDS_BUCKET_NAME = "logs";
    public static final String IMPORT_CARDS_BUCKET_DIRECTORY_NAME = "import-cards";
    public static final String IMPORT_CARDS_EMAIL_SUBJECT = "Cards Import Job Report";

    public static final String UPDATE_CARDS_FILE_PREFIX = "update-cards-";
    public static final String UPDATE_CARDS_BUCKET_NAME = "logs";
    public static final String UPDATE_CARDS_BUCKET_DIRECTORY_NAME = "update-cards";
    public static final String UPDATE_CARDS_EMAIL_SUBJECT = "Cards Update Job Report";

    private Constants() {
    }
}
