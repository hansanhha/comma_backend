package com.know_wave.comma.comma_backend.web.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class DBExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity pkOrUniqueDuplicateException(DataIntegrityViolationException ex) {
        return new ResponseEntity("잘못된 값에 의해 거절되었습니다", HttpStatus.BAD_REQUEST);
    }

}
