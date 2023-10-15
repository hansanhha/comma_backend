package know_wave.comma.payment.dto;

import know_wave.comma.payment.entity.PaymentType;

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
