package know_wave.comma.payment.service;

import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundRequest;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.dto.gateway.PaymentGatewayApproveRequest;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutRequest;
import know_wave.comma.payment.dto.kakaopay.KakaopayApproveRequest;
import know_wave.comma.payment.dto.kakaopay.KakaopayReadyRequest;
import know_wave.comma.payment.dto.kakaopay.KakaopayRefundRequest;
import know_wave.comma.payment.entity.PaymentCbUrl;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentClientManager {

    private final List<PaymentClient> paymentClients;
    private final PaymentCbUrl paymentCbUrl;
    private final Map<PaymentFeature, String> itemNameMap = new HashMap<>();

    public PaymentClientReadyResponse ready(PaymentGatewayCheckoutRequest checkoutDto, String paymentRequestId) {
        PaymentType type = checkoutDto.getPaymentType();
        PaymentClient paymentClient = getPaymentClient(type);

        switch (type) {
            case KAKAO_PAY:
                var kakaopayReadyRequest = KakaopayReadyRequest.of(
                        paymentCbUrl.getMap(),
                        checkoutDto,
                        paymentRequestId);
                return paymentClient.ready(kakaopayReadyRequest);
            default:
                throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE);
        }
    }

    public PaymentClientApproveResponse approve(PaymentGatewayApproveRequest request, String tid) {
        PaymentType paymentType = PaymentType.valueOf(request.getPaymentType());
        PaymentClient paymentClient = getPaymentClient(paymentType);

        switch (paymentType) {
            case KAKAO_PAY:
                var kakaopayApproveRequest = KakaopayApproveRequest.of(paymentCbUrl.getMap().get(PaymentCbUrl.CID_KEY),
                        request.getAccountId(),
                        request.getPaymentRequestId(),
                        tid,
                        request.getPgToken());
                return paymentClient.approve(kakaopayApproveRequest);
            default:
                throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE);
        }
    }

    public PaymentClientRefundResponse refund(PaymentClientRefundRequest refundDto) {
        PaymentType pyamentType = refundDto.getPaymentType();
        PaymentClient paymentClient = getPaymentClient(pyamentType);

        switch (pyamentType) {
            case KAKAO_PAY:
                var kakaopayRefundRequest = KakaopayRefundRequest.of(paymentCbUrl.getMap().get(PaymentCbUrl.CID_KEY),
                        refundDto.getTid(),
                        refundDto.getAmount(),
                        0);
                return paymentClient.refund(kakaopayRefundRequest);
            default:
                throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE);
        }
    }

    private PaymentClient getPaymentClient(PaymentType type) {
        return paymentClients.stream()
                .filter(service -> service.isSupport(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE));
    }

}
