package com.know_wave.comma.comma_backend.util.message;

import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageSource {

    public static final String NOT_AUTHENTICATED_REQUEST = "Not authenticated request";
    public static final String UNABLE_SEND_EMAIL = "Unable to send email";
    public static final String ALREADY_VERIFIED_EMAIL = "Already verified email";
    public static final String NOT_VERIFIED_EMAIL = "Not verified email";
    public static final String NOT_FOUND_EMAIL = "Not found email";
    public static final String BAD_CREDENTIALS = "Bad credentials";
    public static final String NOT_EXIST_ACCOUNT = "Not exist account";
    public static final String NOT_EXIST_CATEGORY = "Not exist category";
    public static final String NOT_FOUND_TOKEN = "Not found token";
    public static final String TEMPERED_TOKEN = "Tempered token";
    public static final String NOT_FOUND_ACCESS_TOKEN = "Not found access token";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String EXPIRED_REFRESH_TOKEN = "Expired refresh token. login is required";
    public static final String EXPIRED_ACCESS_TOKEN = "Expired access token. re-issuance is required";
    public static final String NOT_FOUND_ARDUINO = "Not found arduino";
    public static final String NOT_FOUND_COMMENT = "Not found comment";
    public static final String ALREADY_EXIST_ARDUINO = "Already exist arduino";
    public static final String ALREADY_EXIST_VALUE = "Already exist value";
    public static final String PERMISSION_DENIED = "Permission denied";
    public static final String NOT_FOUND_VALUE = "Not found value";
    public static final String NOT_EXIST_VALUE = "Not found value";
    public static final String ALREADY_IN_BASKET = "Already in basket";
    public static final String NOT_ACCEPTABLE_REQUEST = "Not acceptable request";
    public static final String INVALID_VALUE = "Invalid value";
    public static final String NOT_SUPPORTED_PAYMENT_TYPE = "Not supported payment type";
    public static final String ALREADY_PAID = "Already paid";
    public static final String ALREADY_REFUNDED = "Already refunded";
}
