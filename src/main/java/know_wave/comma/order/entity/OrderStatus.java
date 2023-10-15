package know_wave.comma.order.entity;

public enum OrderStatus {

    APPLIED("신청 완료"),
    PREPARING("준비 중"),
    CANCELLATION_REQUEST("주문 취소 요청"),
    REJECTED("요청 거부"),
    CANCELED("주문 취소"),
    READY("준비 완료"),
    RECEIVED_COMPLETED("수령 완료");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean changeableTo(OrderStatus status) {
        return switch (this) {
            case APPLIED -> status == PREPARING || status == CANCELLATION_REQUEST || status == REJECTED || status == CANCELED;
            case PREPARING -> status == READY || status == CANCELLATION_REQUEST || status == REJECTED || status == CANCELED ;
            case CANCELLATION_REQUEST -> status == CANCELED || status == READY;
            case READY -> status == RECEIVED_COMPLETED || status == CANCELED;
            case REJECTED, CANCELED, RECEIVED_COMPLETED -> false;
        };
    }

    public String getNotificationTitle() {
        return switch (this) {
            case APPLIED -> "컴마 실습재료 신청 알림";
            case PREPARING -> "컴마 실습재료 준비 중 알림";
            case CANCELLATION_REQUEST -> "컴마 실습재료 주문 취소 요청 알림";
            case REJECTED -> "컴마 실습재료 요청 거부 알림";
            case CANCELED -> "컴마 실습재료 주문 취소 알림";
            case READY -> "컴마 실습재료 준비 완료 알림";
            case RECEIVED_COMPLETED -> "컴마 실습재료 수령 완료 알림";
        };
    }

    public static String GET_ORDER_STATUS_MSG(OrderStatus status) {
        return status.getValue() + " 상태입니다";
    }
    public static String GET_IMMUTABLE_ORDER_STATUS_MSG(OrderStatus status) {
        return status.getValue() + " 상태로 변경할 수 없습니다";
    }
}
