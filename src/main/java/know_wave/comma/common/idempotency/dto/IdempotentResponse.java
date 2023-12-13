package know_wave.comma.common.idempotency.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IdempotentResponse<T> {

    private final int responseStatus;
    private final T response;

    public static <T> IdempotentResponse<T> of(int responseStatus, T response) {
        return new IdempotentResponse<T>(responseStatus, response);
    }
}
