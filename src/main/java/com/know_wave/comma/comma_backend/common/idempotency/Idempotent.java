package com.know_wave.comma.comma_backend.common.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotent extends BaseTimeEntity {

    public Idempotent(String idempotentKey, String httpMethod, String apiPath, int responseStatus, String responseMessage, String payload) {
        this.idempotentKey = idempotentKey;
        this.httpMethod = httpMethod;
        this.apiPath = apiPath;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.payload = payload;
    }
    @Id
    @GeneratedValue
    @Column(name = "idempotent_id")
    private Long id;

    private String idempotentKey;

    private String httpMethod;

    private String apiPath;

    private int responseStatus;

    @Column(columnDefinition = "TEXT")
    private Object responseMessage;

    @Column(columnDefinition = "TEXT")
    private String payload;

}
