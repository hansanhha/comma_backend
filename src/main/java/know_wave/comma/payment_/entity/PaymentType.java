package know_wave.comma.payment_.entity;

public enum PaymentType {

    KAKAO("카카오페이");

    private final String paymentName;

    PaymentType(String paymentName) {
        this.paymentName = paymentName;
    }

    @Override
    public String toString() {
        return paymentName;
    }

}
