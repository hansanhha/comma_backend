package com.know_wave.comma.comma_backend.security.service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Stream;


@Service
public class PermitRequestMatcherService {

    private List<RequestMatcher> userPermitRequestMatchers;
    private List<RequestMatcher> adminPermitRequestMatchers;
    private List<RequestMatcher> allPermitRequestMatchers;
    private final HandlerMappingIntrospector handlerIntroceptor;

    public PermitRequestMatcherService(HandlerMappingIntrospector handlerIntroceptor) {
        this.handlerIntroceptor = handlerIntroceptor;
    }

    @PostConstruct
    private void init() {
        MvcRequestMatcher getArduino = new MvcRequestMatcher(handlerIntroceptor, "/arduino/*");
        getArduino.setMethod(HttpMethod.GET);
        MvcRequestMatcher getArduinoList = new MvcRequestMatcher(handlerIntroceptor, "/arduinos/**");
        getArduinoList.setMethod(HttpMethod.GET);

        userPermitRequestMatchers = List.of(
                new MvcRequestMatcher(handlerIntroceptor, "/account/signin"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/signup"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/r"),
                new MvcRequestMatcher(handlerIntroceptor, "/account/email/verify"),
                getArduino,
                getArduinoList
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
