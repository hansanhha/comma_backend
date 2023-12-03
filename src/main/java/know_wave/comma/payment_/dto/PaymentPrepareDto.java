package know_wave.comma.payment_.dto;

import know_wave.comma.payment_.entity.PaymentType;

public record PaymentPrepareDto(
        String tempOrderId,
        PaymentType paymentType) {

        public static PaymentPrepareDto of(PaymentRequest request) {
                return new PaymentPrepareDto(
                        request.getArduinoOrderId(),
                        request.getPaymentType()
                );
        }
}
