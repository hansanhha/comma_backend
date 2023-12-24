package know_wave.comma.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentFeature {

    ARDUINO_DEPOSIT("아두이노 주문 보증금"),
    SECOND_HAND("중고거래"),
    DONATION("기부");

    private final String feature;
}
