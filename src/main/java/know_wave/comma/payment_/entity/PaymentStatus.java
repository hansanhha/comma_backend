package know_wave.comma.payment_.entity;

public enum PaymentStatus {

    REQUEST("결제 요청"),
    APPROVE("결제 승인"),
    CANCEL("결제 취소"),
    TIME_OUT("결제 시간 초과"),
    FAIL("결제 실패");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
