package know_wave.comma.common.idempotency.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IdempotentRequest {

    private final String idempotentKey;
    private final String httpMethod;
    private final String apiPath;
    private final String payload;

    public static IdempotentRequest create(String idempotentKey, String httpMethod, String apiPath, String payload) {
        return new IdempotentRequest(idempotentKey, httpMethod, apiPath, payload);
    }
}

