package com.know_wave.comma.comma_backend.security.interceptor;

import com.know_wave.comma.comma_backend.security.auth.SecurityAccount;
import com.know_wave.comma.comma_backend.security.filter.LoggingFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

public class LoggingInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request instanceof SecurityContextHolderAwareRequestWrapper securityWrapper) {
            var userPrincipal = (UsernamePasswordAuthenticationToken) securityWrapper.getUserPrincipal();

            if (userPrincipal != null) {
                try {
                    var account = (SecurityAccount) userPrincipal.getPrincipal();

                    if (account != null) {
                        log.info("Client login ID : {}", account.getUsername());
                        log.info("Invoked handler : " + handler);
                    }
                } catch (ClassCastException ex) {
                    return HandlerInterceptor.super.preHandle(request, response, handler);
                }
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if (request instanceof SecurityContextHolderAwareRequestWrapper securityWrapper) {
            if (securityWrapper.getRequest() instanceof ContentCachingRequestWrapper requestWrapper) {
                log.info("Client payload = {}", LoggingFilter.getPayload(requestWrapper.getContentType(), requestWrapper.getContentAsByteArray()));
            }
        }

        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if (ex != null) {
            log.error("Server error occurred");
            log.error("- location : {}", handler);
            log.error("- exception : {}", ex.toString());

            if (request instanceof  SecurityContextHolderAwareRequestWrapper securityWrapper) {
                if (securityWrapper.getRequest() instanceof ContentCachingRequestWrapper requestWrapper) {
                    log.error("- Client request payload : {}", LoggingFilter.getPayload(requestWrapper.getContentType(), requestWrapper.getContentAsByteArray()));
                }
            } else if (request instanceof ContentCachingRequestWrapper requestWrapper) {
                log.error("- Client request payload : {}", LoggingFilter.getPayload(requestWrapper.getContentType(), requestWrapper.getContentAsByteArray()));
            }
        }

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
