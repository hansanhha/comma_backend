package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.order.entity.OrderInfo;
import com.know_wave.comma.comma_backend.order.service.user.OrderInfoQueryService;
import com.know_wave.comma.comma_backend.common.exception.NotFoundException;
import com.know_wave.comma.comma_backend.payment.exception.AlreadyPaidException;
import com.know_wave.comma.comma_backend.payment.exception.AlreadyRefundedException;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.DepositStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentManager {

    private final List<PaymentService> paymentServices;
    private final OrderInfoQueryService orderInfoQueryService;
    private final DepositQueryService depositQueryService;

    public PaymentService getPaymentService(PaymentType paymentType) throws NotFoundException {
        return paymentServices.stream()
                .filter(paymentService -> paymentService.supports(paymentType))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }

    public void checkAlreadyPaid(String orderNumber) throws AlreadyPaidException {
        Optional<OrderInfo> orderInfoOptional = orderInfoQueryService.optionalFetchAccount(orderNumber);

        if (orderInfoOptional.isPresent()) {
            OrderInfo orderInfo = orderInfoOptional.get();
            List<Deposit> deposits = depositQueryService.getDeposits(orderInfo.getAccount(), orderInfo);

            if (deposits.isEmpty()) {
                return;
            }

            boolean paid = deposits.stream().anyMatch(deposit -> deposit.getDepositStatus().equals(DepositStatus.PAID));

            if (paid) {
                throw new AlreadyPaidException(ExceptionMessageSource.ALREADY_PAID);
            }
        }
    }

    public void checkAlreadyRefund(Deposit deposit) throws AlreadyRefundedException {
        if (deposit.getDepositStatus().equals(DepositStatus.REFUND)) {
            throw new AlreadyRefundedException(ExceptionMessageSource.ALREADY_REFUNDED);
        }
    }
}
