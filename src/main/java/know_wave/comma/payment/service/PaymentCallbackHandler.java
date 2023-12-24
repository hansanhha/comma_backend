package know_wave.comma.payment.service;

import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.PaymentFeature;

public interface PaymentCallbackHandler {

    CompleteCallbackResponse complete(CompleteCallback completeCallback);

    CancelCallbackResponse cancel(CancelCallback cancelCallback);

    FailCallbackResponse fail(FailCallback failCallback);

    boolean isSupport(PaymentFeature paymentFeature);
}
