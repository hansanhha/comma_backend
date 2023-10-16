package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.Payment;
import com.know_wave.comma.comma_backend.payment.entity.PaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.exception.NotFoundException;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPaymentProvider implements PaymentProvider<Payment, PaymentApprove>{

    private final List<PaymentService<Payment, PaymentApprove>> paymentServices;

    public DefaultPaymentProvider(List<PaymentService<Payment, PaymentApprove>> paymentServices) {
        this.paymentServices = paymentServices;
    }

    @Override
    public PaymentService<Payment, PaymentApprove> getPaymentService(PaymentType paymentType) throws NotFoundException {
        return paymentServices.stream()
                .filter(paymentService -> paymentService.supports(paymentType))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }
}
