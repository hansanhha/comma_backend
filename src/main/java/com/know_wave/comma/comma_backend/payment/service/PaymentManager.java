package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.arduino.service.normal.OrderQueryService;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.DepositStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.common.exception.NotFoundException;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentManager {

    private final List<PaymentService> paymentServices;
    private final AccountQueryService accountQueryService;
    private final OrderQueryService orderQueryService;
    private final CommaArduinoDepositPolicy policy;

    public PaymentService getPaymentService(PaymentType paymentType) throws NotFoundException {
        return paymentServices.stream()
                .filter(paymentService -> paymentService.supports(paymentType))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }

    public Deposit createEntity(PaymentAuthRequest request, String paymentRequestId, String transactionId) {
        Account account = accountQueryService.findAccount(request.accountId());
        OrderInfo orderInfo = orderQueryService.getOrderInfoById(request.arduinoOrderId());

        return new Deposit(account,
                orderInfo,
                paymentRequestId,
                transactionId,
                DepositStatus.NONE,
                PaymentStatus.REQUEST,
                request.paymentType(),
                null,
                policy.getAmount(),
                policy.getProductName(),
                true,
                true
                );
    }
}
