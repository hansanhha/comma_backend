package com.know_wave.comma.comma_backend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            loggingBeforeChain(requestWrapper);
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            loggingAfterChain(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void loggingBeforeChain(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();

        log.info("Client info = IP : {}",
                request.getRemoteAddr()
        );

        log.info("Client request = [{}] {} content-type : {}",
                request.getMethod(),
                queryString != null ? request.getRequestURI() + "?" + queryString : request.getRequestURI(),
                request.getContentType()
        );
    }

    private void loggingAfterChain(ContentCachingResponseWrapper response) {
        log.info("Server response : {} payload: {}",
                response.getStatus(),
                getPayload(response.getContentType(), response.getContentAsByteArray())
        );
    }

    public static String getPayload(String contentType, byte[] contentData) {

        boolean isVisible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));

        if (isVisible && contentData.length > 0) {
            return new String(contentData);
        } else if (contentData.length == 0){
            return "empty";
        }else {
            return "binary data";
        }

    }

    private static boolean isVisible(MediaType contentType) {
        List<MediaType> allowedTypes = List.of(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.valueOf("application/*+json"),
                MediaType.valueOf("text/*")
        );

        return allowedTypes.stream()
                .anyMatch(allowedType -> allowedType.includes(contentType));
    }


}
