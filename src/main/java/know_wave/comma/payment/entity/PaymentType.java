package know_wave.comma.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {

    KAKAO_PAY("카카오페이"),
    TOSS_PAY("토스페이"),
    NAVER_PAY("네이버페이"),
    PAYCO_PAY("페이코페이"),
    CREDIT_CARD("신용카드");

    private final String type;
}
