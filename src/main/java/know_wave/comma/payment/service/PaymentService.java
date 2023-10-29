package know_wave.comma.payment.service;

import know_wave.comma.order.dto.OrderInfoDto;
import know_wave.comma.payment.dto.PaymentPrepareDto;
import know_wave.comma.payment.dto.PaymentPrepareResult;
import know_wave.comma.payment.dto.PaymentRefundResult;
import know_wave.comma.payment.entity.Deposit;
import know_wave.comma.payment.entity.PaymentType;

public interface PaymentService {

    PaymentPrepareResult ready(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfo);

    PaymentPrepareResult readyWithSSE(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfo, String sseId);

    void pay(Deposit deposit, String tempOrderId, String paymentToken);

    PaymentRefundResult refund(Deposit request);

    void cancel(Deposit request);

    boolean supports(PaymentType type);
}
