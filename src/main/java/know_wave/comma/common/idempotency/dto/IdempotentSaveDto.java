package know_wave.comma.common.idempotency.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IdempotentSaveDto<T> {

    private final String idempotentKey;
    private final String httpMethod;
    private final String apiPath;
    private final Integer responseStatus;
    private final T response;
    private final String payload;

    public static <T> IdempotentSaveDto<T> of(String idempotentKey, String httpMethod, String apiPath, Integer responseStatus, T response, String payload) {
        return new IdempotentSaveDto<>(idempotentKey, httpMethod, apiPath, responseStatus, response, payload);
    }
}
