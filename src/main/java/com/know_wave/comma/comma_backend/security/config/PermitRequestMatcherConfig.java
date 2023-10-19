package com.know_wave.comma.comma_backend.security.config;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Stream;


@Configuration
public class PermitRequestMatcherConfig {

    private List<RequestMatcher> userPermitRequestMatchers;
    private List<RequestMatcher> adminPermitRequestMatchers;
    private List<RequestMatcher> allPermitRequestMatchers;
    private final HandlerMappingIntrospector handlerIntroceptor;

    public PermitRequestMatcherConfig(HandlerMappingIntrospector handlerIntroceptor) {
        this.handlerIntroceptor = handlerIntroceptor;
    }

    @PostConstruct
    private void init() {
        MvcRequestMatcher getArduino = new MvcRequestMatcher(handlerIntroceptor, "/arduino/*");
        MvcRequestMatcher getArduinoList = new MvcRequestMatcher(handlerIntroceptor, "/arduinos/**");
        MvcRequestMatcher getPaymentRedirect = new MvcRequestMatcher(handlerIntroceptor, "/api/v1/payment/**");
        MvcRequestMatcher sseConnect = new MvcRequestMatcher(handlerIntroceptor, "/sse/connect");
        getArduino.setMethod(HttpMethod.GET);
        getArduinoList.setMethod(HttpMethod.GET);
        getPaymentRedirect.setMethod(HttpMethod.GET);
        sseConnect.setMethod(HttpMethod.GET);

        userPermitRequestMatchers = List.of(
                new MvcRequestMatcher(handlerIntroceptor, "/account/signin"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/signup"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/r"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/verify"),
                getArduino,
                getArduinoList,
                getPaymentRedirect,
                sseConnect
        );

        adminPermitRequestMatchers = List.of(
                new MvcRequestMatcher(handlerIntroceptor, "/admin/account/signup")
        );

        allPermitRequestMatchers = Stream.concat(
                userPermitRequestMatchers.stream(),
                adminPermitRequestMatchers.stream()
        ).toList();
    }

    public boolean isPermitRequest(HttpServletRequest request) {
        return allPermitRequestMatchers.stream().anyMatch(permitRequestMatcher -> permitRequestMatcher.matches(request));
    }

    public List<RequestMatcher> getUserPermitRequestMatchers() {
        return userPermitRequestMatchers;
    }

    public List<RequestMatcher> getAdminPermitRequestMatchers() {
        return adminPermitRequestMatchers;
    }

    public List<RequestMatcher> getAllPermitRequestMatchers() {
        return allPermitRequestMatchers;
    }
}
