package com.know_wave.comma.comma_backend.web.advice;


import com.know_wave.comma.comma_backend.common.sse.SseEmitterSendException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestControllerAdvice.class)
public class CommonExceptionAdvice {

    @ExceptionHandler(SseEmitterSendException.class)
    public ResponseEntity<String> sseEmitterSendException(SseEmitterSendException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
