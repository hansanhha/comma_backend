package com.know_wave.comma.comma_backend.common.idempotency;

import com.know_wave.comma.comma_backend.common.idempotency.exception.IdempotencyResponse;
import com.know_wave.comma.comma_backend.common.idempotency.exception.IdempotencyUnprocessableEntity;
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

    @Before("@annotation(com.know_wave.comma.comma_backend.common.idempotency.Idempotency) || @within(com.know_wave.comma.comma_backend.common.idempotency.Idempotency)")
    public void validateIdempotentKey(JoinPoint joinPoint) throws RuntimeException {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof IdempotentDto idempotentDto) {
                idempotentKeyRepository.findByIdempotentInfo(idempotentDto.idempotentKey(), idempotentDto.httpMethod(), idempotentDto.apiPath())
                        .ifPresent(
                                idempotent -> {
                                    if (!idempotent.getPayload().equals(idempotentDto.payload())) {
                                        log.info("IdempotenceAspect error: Idempotency unprocessable entity");
                                        throw new IdempotencyUnprocessableEntity("Payload is not matched");
                                    }
                                    log.info("Idempotency response");
                                    throw new IdempotencyResponse(idempotent.getResponseStatus()
                                            + ", idempotentResponse: " + idempotent.getResponseMessage());
                                }
                        );
            }
        }
    }
}
