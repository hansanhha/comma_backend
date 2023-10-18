package com.know_wave.comma.comma_backend.payment.dto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

public class HttpEntityCreator {

    public static HttpEntity<MultiValueMap<String, Object>> of(MultiValueMap<String, Object> value) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(value, httpHeaders);
    }
}
