package know_wave.comma.payment.service;

import know_wave.comma.common.entity.ExceptionMessageSource;
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

import static know_wave.comma.payment.entity.PaymentType.KAKAO_PAY;

@Service
@RequiredArgsConstructor
public class PaymentClientManager {

    private final List<PaymentClient> paymentClients;
    private final PaymentCbUrl paymentCbUrl;
    private final Map<PaymentFeature, String> itemNameMap = new HashMap<>();

    public PaymentClientReadyResponse ready(PaymentGatewayCheckoutRequest checkoutDto, String paymentRequestId) {
        PaymentType paymentType = checkoutDto.getPaymentType();
        PaymentClient paymentClient = getPaymentClient(paymentType);

        switch (paymentType) {
            case KAKAO_PAY:
                KakaoPayClient kakaoPayClient = (KakaoPayClient) paymentClient;

                var kakaopayReadyRequest = KakaopayReadyRequest.create(
                        paymentCbUrl.getMap(paymentType),
                        checkoutDto,
                        paymentRequestId);

                return kakaoPayClient.ready(kakaopayReadyRequest);
            default:
                throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE);
        }
    }

    public PaymentClientApproveResponse approve(PaymentGatewayApproveRequest approveRequest, String tid) {
        PaymentType paymentType = approveRequest.getPaymentType();
        PaymentClient paymentClient = getPaymentClient(paymentType);

        switch (paymentType) {
            case KAKAO_PAY:
                KakaoPayClient kakaoPayClient = (KakaoPayClient) paymentClient;

                var kakaopayApproveRequest = KakaopayApproveRequest.create(
                        paymentCbUrl.getMap(paymentType).get(PaymentCbUrl.CID_KEY),
                        tid,
                        approveRequest.getPaymentRequestId(),
                        approveRequest.getAccountId(),
                        approveRequest.getPgToken());

                return kakaoPayClient.approve(kakaopayApproveRequest);
            default:
                throw new IllegalArgumentException(ExceptionMessageSource.NOT_SUPPORTED_PAYMENT_TYPE);
        }
    }

    public PaymentClientRefundResponse refund(PaymentClientRefundRequest refundRequest) {
        PaymentType paymentType = refundRequest.getPaymentType();
        PaymentClient paymentClient = getPaymentClient(paymentType);

        switch (paymentType) {
            case KAKAO_PAY:
                KakaoPayClient kakaoPayClient = (KakaoPayClient) paymentClient;

                var kakaopayRefundRequest = KakaopayRefundRequest.create(
                        paymentCbUrl.getMap(paymentType).get(PaymentCbUrl.CID_KEY),
                        refundRequest.getTid(),
                        refundRequest.getAmount(),
                        0);
                return kakaoPayClient.refund(kakaopayRefundRequest);
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
