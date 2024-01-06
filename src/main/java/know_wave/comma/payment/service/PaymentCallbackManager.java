package know_wave.comma.payment.service;

import jakarta.transaction.Transactional;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.PaymentFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentCallbackManager {

    private final List<PaymentCallbackHandler> paymentCallbackServices;

    public CompleteCallbackResponse complete(CompleteCallback completeCallback) {
        return getPaymentCallback(completeCallback.getPaymentFeature())
                .complete(completeCallback);
    }

    public CancelCallbackResponse cancel(CancelCallback cancelCallback) {
        return getPaymentCallback(cancelCallback.getPaymentFeature())
                .cancel(cancelCallback);
    }

    public FailCallbackResponse fail(FailCallback failCallback) {
        return getPaymentCallback(failCallback.getPaymentFeature())
                .fail(failCallback);
    }

    public void error(ErrorCallback errorCallback) {
        getPaymentCallback(errorCallback.getPaymentFeature())
                .error(errorCallback);
    }

    private PaymentCallbackHandler getPaymentCallback(PaymentFeature paymentFeature) {
        return paymentCallbackServices.stream()
                .filter(paymentCallback -> paymentCallback.isSupport(paymentFeature))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_FEATURE));
    }
}
