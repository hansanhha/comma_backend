package know_wave.comma.common.idempotency.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    TRACE("TRACE");

    private final String method;
}
