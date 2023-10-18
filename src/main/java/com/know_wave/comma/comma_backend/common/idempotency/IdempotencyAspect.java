package com.know_wave.comma.comma_backend.common.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.know_wave.comma.comma_backend.common.exception.idempotency.IdempotencyResponse;
import com.know_wave.comma.comma_backend.common.exception.idempotency.IdempotencyUnprocessableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class IdempotencyAspect {

    private final IdempotentKeyRepository idempotentKeyRepository;

    @Before("@annotation(com.know_wave.comma.comma_backend.common.idempotency.Idempotency)")
    public void validateIdempotentKey(JoinPoint joinPoint) throws RuntimeException {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof IdempotentDto idempotentDto) {
                idempotentKeyRepository.findById(idempotentDto.key())
                        .ifPresent(
                                idempotent -> {
                                    if (!idempotent.getPayload().equals(idempotentDto.payload())) {
                                        log.info("Idempotency unprocessable entity");
                                        throw new IdempotencyUnprocessableEntity("Payload is not matched");
                                    }
                                    log.info("Idempotency response");

                                    throw new IdempotencyResponse(idempotent.getResponseStatus() + ", idempotentResponse: " + idempotent.getResponseMessage());
                                }
                        );
            }
        }
    }
}
