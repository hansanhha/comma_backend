package know_wave.comma.arduino.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    VALID("유효한 주문"),
    DEPOSIT_PAYMENT_REQUIRED("보증금 결제 대기"),
    FAILURE_CAUSE_DEPOSIT_FAILURE("주문 실패(보증금 결제 실패)"),
    FAILURE_CAUSE_DEPOSIT_CANCEL("주문 실패(보증금 결제 취소)"),
    FAILURE_CAUSE_ARDUINO_STOCK("주문 실패(재고 부족)"),
    FAILURE_CAUSE_ARDUINO_STOCK_STATUS("주문 실패(재고 상태)"),
    FAILURE_CAUSE_REJECTED("주문 실패(주문 거부됨)"),
    ORDERED("주문 완료"),
    FREE_CANCEL("주문취소"),
    CANCEL("주문취소"),
    PREPARING("주문 준비 중"),
    BE_READY("주문 준비 완료"),
    RECEIVE("주문 수령");

    private final String status;

    private static final List<OrderStatus> FAILURES = List.of(
            FAILURE_CAUSE_DEPOSIT_FAILURE,
            FAILURE_CAUSE_DEPOSIT_CANCEL,
            FAILURE_CAUSE_ARDUINO_STOCK,
            FAILURE_CAUSE_ARDUINO_STOCK_STATUS,
            FAILURE_CAUSE_REJECTED
    );

    public boolean isOrdered() {
        return this == ORDERED;
    }

    public boolean isFailure() {
        return FAILURES.contains(this);
    }

    public boolean isFailureCauseStock() {
        return this == FAILURE_CAUSE_ARDUINO_STOCK;
    }

    public boolean isFailureCauseStockStatus() {
        return this == FAILURE_CAUSE_ARDUINO_STOCK_STATUS;
    }

    public boolean isCancel() {
        return this == CANCEL || this == FREE_CANCEL;
    }
}
