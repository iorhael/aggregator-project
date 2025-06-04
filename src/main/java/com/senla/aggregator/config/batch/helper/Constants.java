package com.senla.aggregator.config.batch.helper;

public final class Constants {
    public static final String IMPORT_CARDS_FILE_PREFIX = "import-cards-";
    public static final String UPDATE_CARDS_FILE_PREFIX = "update-cards-";
    public static final String IMPORT_CARDS_JOB_NAME = "importProductCardsJob";
    public static final String UPDATE_CARDS_JOB_NAME = "updateProductCardsJob";
    public static final String EXPORT_CARDS_JOB_NAME = "exportProductCardsJob";
    public static final String VERIFIED_PRODUCTS_ONLY_PARAM = "verifiedProductsOnly";
    public static final String RETAILER_ID_PARAM = "retailerId";
    public static final String RETAILER_NAME_PARAM = "retailerName";
    public static final String CONTACT_EMAIL_PARAM = "contactEmail";
    public static final String CONTENT_TYPE_PARAM = "contentType";
    public static final String TIME_PARAM = "time";
    public static final String IMPORT_FILE_PARAM = "importFile";
    public static final String EXPORT_FILE_PARAM = "exportFile";
    public static final String JSON_IMPORT_READER_NAME = "jsonImportReader";
    public static final String XML_IMPORT_READER_NAME = "xmlImportReader";
    public static final String CSV_IMPORT_READER_NAME = "csvImportReader";
    public static final String JSON_EXPORT_WRITER_NAME = "jsonImportWriter";
    public static final String XML_EXPORT_WRITER_NAME = "xmlImportWriter";
    public static final String CSV_EXPORT_WRITER_NAME = "csvImportWriter";
    public static final String MAIN_STEP_NAME = "mainStep";
    public static final String PRODUCT_CARD_NAME = "productCard";
    public static final String PRODUCT_CARDS_NAME = "productCards";
    public static final String CSV_DELIMITER = ",";
    public static final String INVALID_CONTENT_TYPE = "Invalid content type: %s";
    public static final String[] PRODUCT_CARD_FIELDS_FILE = {"product_name", "description", "warranty", "installment_period", "max_delivery_time", "price"};
    public static final String[] PRODUCT_CARD_FIELDS_MODEL = {"productName", "description", "warranty", "installmentPeriod", "maxDeliveryTime", "price"};

    private Constants() {
    }
}
