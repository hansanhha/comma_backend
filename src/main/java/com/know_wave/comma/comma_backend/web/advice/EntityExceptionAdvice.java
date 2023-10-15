package com.know_wave.comma.comma_backend.web.advice;

import com.know_wave.comma.comma_backend.util.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.util.exception.EmailVerifiedException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.INVALID_VALUE;

@RestControllerAdvice(annotations = RestController.class)
public class EntityExceptionAdvice {

    private final MessageSource messageSource;

    public EntityExceptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({NotFoundEmailException.class, EmailVerifiedException.class})
    public ResponseEntity<String> EmailVerifyException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class, EntityExistsException.class})
    public ResponseEntity<String> EntityExistenceException(PersistenceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtException(ExpiredJwtException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<String> serviceLayerPermissionException(AuthorizationServiceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // jpa entity throw exception when entity is not valid
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> entityConstraintViolationException(ConstraintViolationException ex, Locale locale) {

        List<String> messageTemplateList = ex.getConstraintViolations()
                .stream()
                .map(constraintViolation -> constraintViolation.getMessageTemplate())
                .toList();

        String message = messageTemplateList
                .stream()
                .map(messageTemplate -> messageSource.getMessage(messageTemplate, null, INVALID_VALUE, locale))
                .collect(Collectors.joining(","));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
