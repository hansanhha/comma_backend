package know_wave.comma.payment_.service;

import know_wave.comma.order_.dto.OrderInfoDto;
import know_wave.comma.payment_.dto.PaymentPrepareDto;
import know_wave.comma.payment_.dto.PaymentPrepareResult;
import know_wave.comma.payment_.dto.PaymentRefundResult;
import know_wave.comma.payment_.entity.Deposit;
import know_wave.comma.payment_.entity.PaymentType;

public interface PaymentService {

    PaymentPrepareResult ready(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfo);

    PaymentPrepareResult readyWithSSE(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfo, String sseId);

    void pay(Deposit deposit, String tempOrderId, String paymentToken);

    PaymentRefundResult refund(Deposit request);

    void cancel(Deposit request);

    boolean supports(PaymentType type);
}
