package com.know_wave.comma.comma_backend.common.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record IdempotentDto(String idempotentKey, String httpMethod, String apiPath, String payload) {

    public static Idempotent of(IdempotentDto idempotentDto, int responseStatus, Object responseMessage) {
        ObjectMapper mapper = new ObjectMapper();
        String response = "";

        try {
            response = mapper.writeValueAsString(responseMessage);
        } catch (JsonProcessingException e) {
            log.error("IdempotentDto error: can't convert responseMessage to json");
        }

        return new Idempotent(idempotentDto.idempotentKey, idempotentDto.httpMethod, idempotentDto.apiPath, responseStatus, response, idempotentDto.payload);
    }
}
