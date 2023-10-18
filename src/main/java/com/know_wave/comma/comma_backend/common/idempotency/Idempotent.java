package com.know_wave.comma.comma_backend.common.idempotency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotent extends BaseTimeEntity implements Persistable<String> {

    public Idempotent(String idempotentKey, String httpMethod, String apiPath, int responseStatus, String responseMessage, String payload) {
        this.idempotentKey = idempotentKey;
        this.httpMethod = httpMethod;
        this.apiPath = apiPath;
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.payload = payload;
    }

    @Id
    @Column(name = "idempotentKey_id")
    private String idempotentKey;

    private String httpMethod;

    private String apiPath;

    private int responseStatus;

    @Column(columnDefinition = "TEXT")
    private Object responseMessage;

    @Column(columnDefinition = "TEXT")
    private String payload;


    @Override
    public String getId() {
        return idempotentKey;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }
}
