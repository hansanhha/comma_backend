package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResponse;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.DepositStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.payment.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentGateway {

    private final DepositQueryService depositQueryService;
    private final PaymentManager paymentManager;
    private final DepositRepository depositRepository;

    public static final String successUrl = "http://localhost:8080/api/v1/payment/%s/success/orders/%s/account/%s";
    public static final String failUrl = "http://localhost:8080/api/v1/payment/%s/fail/orders/%s/account/%s";
    public static final String cancelUrl = "http://localhost:8080/api/v1/payment/%s/cancel/orders/%s/account/%s";

    public PaymentAuthResponse ready(PaymentAuthRequest request) {
        var paymentService = paymentManager.getPaymentService(request.paymentType());

        var paymentAuthResult = paymentService.ready(request);

        Deposit deposit = paymentManager.createEntity(request.paymentType(), request, paymentAuthResult.transactionId());
        depositRepository.save(deposit);

        return new PaymentAuthResponse(paymentAuthResult.redirectMobileWebUrl(), paymentAuthResult.redirectPcWebUrl());
    }

    public void pay(String type, String accountId, String orderId, String paymentToken) {
        PaymentType paymentType = PaymentType.valueOf(type);

        var paymentService = paymentManager.getPaymentService(paymentType);

        Deposit deposit = depositQueryService.findDeposit(accountId, orderId);

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
