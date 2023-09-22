package com.know_wave.comma.comma_backend.web.advice;

import com.know_wave.comma.comma_backend.web.exception.entity.EmailNotFoundException;
import com.know_wave.comma.comma_backend.web.exception.entity.EmailVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class EntityExceptionAdvice {

    @ExceptionHandler({EmailNotFoundException.class, EmailVerifiedException.class})
    public ResponseEntity EmailVerifyException(RuntimeException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
