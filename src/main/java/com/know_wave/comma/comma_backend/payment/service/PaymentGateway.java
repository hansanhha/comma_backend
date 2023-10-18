package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.common.idempotency.Idempotency;
import com.know_wave.comma.comma_backend.common.idempotency.Idempotent;
import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.common.idempotency.IdempotentKeyRepository;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResponse;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.DepositStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.payment.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Idempotency
@Transactional
@RequiredArgsConstructor
public class PaymentGateway {

    private final DepositQueryService depositQueryService;
    private final PaymentManager paymentManager;
    private final DepositRepository depositRepository;
    private final IdempotentKeyRepository idempotentKeyRepository;

    public static final String successUrl = "http://localhost:8080/api/v1/payment/%s/success/%s";
    public static final String failUrl = "http://localhost:8080/api/v1/payment/%s/fail/orders/%s";
    public static final String cancelUrl = "http://localhost:8080/api/v1/payment/%s/cancel/%s";

    @Idempotency
    public PaymentAuthResponse ready(IdempotentDto idempotentDto, PaymentAuthRequest request) {
        var paymentService = paymentManager.getPaymentService(request.paymentType());

        var paymentAuthResult = paymentService.ready(request);

        var result = new PaymentAuthResponse(paymentAuthResult.redirectMobileWebUrl(), paymentAuthResult.redirectPcWebUrl());

        Deposit deposit = paymentManager.createEntity(request, paymentAuthResult.paymentRequestId(), paymentAuthResult.transactionId());
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), result);

        depositRepository.save(deposit);
        idempotentKeyRepository.save(idempotent);

        return result;
    }

    public void pay(String type, String paymentRequestId, String paymentToken) {
        PaymentType paymentType = PaymentType.valueOf(type.toUpperCase());

        var paymentService = paymentManager.getPaymentService(paymentType);

        Deposit deposit = depositQueryService.findByPaymentRequestId(paymentRequestId);

        paymentService.pay(deposit, paymentToken);

        deposit.setPaymentStatus(PaymentStatus.APPROVE);
        deposit.setDepositStatus(DepositStatus.PAID);
    }

    public void refund(PaymentAuthRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        paymentService.refund(request);
    }

    public void cancel(PaymentAuthRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        paymentService.cancel(request);
    }

}
