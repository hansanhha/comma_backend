package know_wave.comma.payment.dto;

import java.time.LocalDateTime;

public record PaymentRefundResult(LocalDateTime refundedDate) {

    public static PaymentRefundResult of(LocalDateTime refundedDate) {
        return new PaymentRefundResult(refundedDate);
    }
}
