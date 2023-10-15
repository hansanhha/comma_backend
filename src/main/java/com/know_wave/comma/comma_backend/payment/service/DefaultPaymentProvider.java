package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.PaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.PaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.util.exception.NotFoundException;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPaymentProvider implements PaymentProvider<PaymentReady, PaymentApprove>{

    private final List<PaymentService<PaymentReady, PaymentApprove>> paymentServices;

    public DefaultPaymentProvider(List<PaymentService<PaymentReady, PaymentApprove>> paymentServices) {
        this.paymentServices = paymentServices;
    }

    @Override
    public PaymentService<PaymentReady, PaymentApprove> getPaymentService(PaymentType paymentType) throws NotFoundException {
        return paymentServices.stream()
                .filter(paymentService -> paymentService.supports(paymentType))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }
}
