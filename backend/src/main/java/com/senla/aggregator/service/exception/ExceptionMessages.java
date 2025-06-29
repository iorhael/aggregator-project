package com.senla.aggregator.service.exception;

public final class ExceptionMessages {
    public static final String KEYCLOAK_CONFLICT = "User already exists";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String KEYCLOAK_BAD_CREDENTIALS = "Bad username or password";
    public static final String STORE_NOT_FOUND = "Store not found";
    public static final String REVIEW_NOT_FOUND = "Review not found";
    public static final String VENDOR_NOT_FOUND = "Vendor not found";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String FAVORITE_NOT_FOUND = "Favorite not found";
    public static final String RETAILER_NOT_FOUND = "Retailer not found";
    public static final String AUTO_UPDATE_INFO_NOT_FOUND = "Auto update info not found";
    public static final String PRODUCT_CARD_NOT_FOUND = "Product card not found";
    public static final String ACCESS_TOKEN_NOT_UPDATED = "Problems with access token update";
    public static final String GMAIL_NOT_SENT = "Mail not sent";
    public static final String INVALID_OBJECT_URL = "Unsupported minio server or bucket url: %s";
    public static final String JOB_LAUNCH_FAILED = "Job launch failed";

    private ExceptionMessages() {
    }
}
