package know_wave.comma.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    REQUEST("결제 요청"),
    COMPLETE("결제 완료"),
    CANCEL("결제 취소"),
    FAILURE("결제 실패"),
    TIME_OUT("결제 시간 초과"),
    REFUND("결제 환불");

    private final String value;

}
