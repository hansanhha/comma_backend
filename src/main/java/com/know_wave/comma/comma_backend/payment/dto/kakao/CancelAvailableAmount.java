package com.know_wave.comma.comma_backend.payment.dto.kakao;

public record CancelAvailableAmount(
        int total,
        int tax_free,
        int vat,
        int point,
        int discount,
        int green_deposit
) {
}
