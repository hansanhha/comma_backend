package know_wave.comma.payment.service;


import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.entity.PaymentType;

// T: payment ready dto
// V: payment approve dto
// R: payment cancel dto
public interface PaymentClient<T, V, R> {

        PaymentClientReadyResponse ready(T t);

        PaymentClientApproveResponse approve(V v);

        PaymentClientRefundResponse refund(R r);

        boolean isSupport(PaymentType type);

        PaymentType getPaymentType();
}
