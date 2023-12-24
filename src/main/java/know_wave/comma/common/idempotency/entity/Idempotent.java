package know_wave.comma.common.idempotency.entity;

import jakarta.persistence.*;
import know_wave.comma.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idempotent extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "idempotent_id")
    private String idempotentKey;

    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;

    private String apiPath;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Integer responseStatus;

    @Column(columnDefinition = "TEXT")
    private String response;

    public static Idempotent create(String idempotentKey, HttpMethod httpMethod, String apiPath, String payload, int responseStatus, String response) {
        return new Idempotent(idempotentKey, httpMethod, apiPath, payload, responseStatus, response);
    }

    @Override
    public String getId() {
        return idempotentKey;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate() == null;
    }

    public boolean equals(String idempotentKey, HttpMethod httpMethod, String apiPath) {
        return this.idempotentKey.equals(idempotentKey)
                && this.httpMethod.equals(httpMethod)
                && this.apiPath.equals(apiPath);
    }
}
