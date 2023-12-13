package know_wave.comma.arduino.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    DEPOSIT_PAYMENT_REQUIRED("보증금 결제 대기"),
    FAILURE_CAUSE_DEPOSIT_FAILURE("주문 실패(보증금 결제 실패)"),
    FAILURE_CAUSE_DEPOSIT_CANCEL("주문 실패(보증금 결제 취소)"),
    FAILURE_CAUSE_ARDUINO_STOCK("주문 실패(재고 부족)"),
    FAILURE_CAUSE_ARDUINO_STATUS("주문 실패(재고 상태)"),
    FAILURE_CAUSE_REJECTED("주문 실패(주문 거부됨)"),
    ORDERED("주문 완료"),
    CANCEL("주문 취소"),
    PREPARING("주문 준비 중"),
    BE_READY("주문 준비 완료"),
    RECEIVE("주문 수령");

    private final String status;
}
