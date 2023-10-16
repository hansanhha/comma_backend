package com.know_wave.comma.comma_backend.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class ApiClientConfig {

    @Value("${kakao.api.key}")
    private String kakaoPayApiKey;

//    @Value("${toss.pay.api.key}")
//    private String tossPayApiKey;

    @Bean("kakaoPayApiClient")
    public RestTemplate kakaoPayApiClient() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor httpRequestInterceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", kakaoPayApiKey);
            request.getHeaders().add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(Collections.singletonList(httpRequestInterceptor));
        return restTemplate;
    }

//    @Bean("tossPayApiClient")
//    public RestTemplate tossPayApiClient() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        ClientHttpRequestInterceptor httpRequestInterceptor = (request, body, execution) -> {
//            request.getHeaders().add("Authorization", tossPayApiKey);
//            request.getHeaders().add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//            return execution.execute(request, body);
//        };
//
//        restTemplate.setInterceptors(Collections.singletonList(httpRequestInterceptor));
//        return restTemplate;
//    }
}
