package com.senla.aggregator.dto;

public final class ValidationPatterns {
    public static final String USERNAME = "^[A-Za-z0-9_.]+$";
    public static final String USERNAME_MESSAGE = "Only latin letters, numbers, underscores and periods are allowed";

    public static final String EMAIL = "^[a-zA-Z0-9.]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,}$";
    public static final String EMAIL_MESSAGE = "Invalid email format";

    public static final String WEBSITE = "^(https?:\\/\\/)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+(?:\\/[^\\s]*)?$";
    public static final String WEBSITE_MESSAGE = "Invalid website format";

    public static final String ONLY_NUMBERS = "^[0-9]*$";
    public static final String ONLY_NUMBERS_MESSAGE = "Only numbers allowed";

    private ValidationPatterns() {
    }
}
