package know_wave.comma.arduino.entity.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {

    FAIL("주문 실패"),
    SUCCESS("주문 완료"),
    CANCEL("주문 취소"),
    PREPARING("주문 준비 중"),
    READY("주문 준비 완료"),
    RECEIVED("주문 수령");

    private final String status;
}
