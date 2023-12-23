package know_wave.comma.payment.controller;

import know_wave.comma.payment.dto.gateway.PaymentGatewayApproveRequest;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCancelRequest;
import know_wave.comma.payment.dto.gateway.PaymentGatewayFailRequest;
import know_wave.comma.payment.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-cb")
@RequiredArgsConstructor
public class PaymentCallbackController {

    private final PaymentGateway paymentGateway;

    @GetMapping("/success/{paymentType}/{paymentFeature}/{accountId}/{paymentRequestId}/{orderNumber}")
    public void success(@PathVariable String paymentType, @PathVariable String paymentFeature,
                        @PathVariable String accountId, @PathVariable String paymentRequestId,
                        @PathVariable String orderNumber,
                        @RequestParam(value = "pg_token") String pgToken) {
        var paymentGatewayApproveRequest = PaymentGatewayApproveRequest.create(paymentRequestId, orderNumber, accountId, paymentType.toUpperCase(), paymentFeature.toUpperCase(), pgToken);
        paymentGateway.approve(paymentGatewayApproveRequest);
    }

    @GetMapping("/cancel/{paymentType}/{accountId}/{paymentFeature}/{paymentRequestId}/{orderNumber}")
    public void cancel(@PathVariable String paymentType, @PathVariable String accountId,
                       @PathVariable String paymentFeature, @PathVariable String paymentRequestId,
                       @PathVariable String orderNumber) {
        var paymentGatewayCancelRequest = PaymentGatewayCancelRequest.create(paymentRequestId, orderNumber, accountId, paymentType, paymentFeature);
        paymentGateway.cancel(paymentGatewayCancelRequest);
    }

    @GetMapping("/fail/{paymentType}/{accountId}/{paymentFeature}/{paymentRequestId}/{orderNumber}")
    public void fail(@PathVariable String paymentType, @PathVariable String accountId,
                     @PathVariable String paymentFeature, @PathVariable String paymentRequestId,
                     @PathVariable String orderNumber) {
        var paymentGatewayFailRequest = PaymentGatewayFailRequest.create(paymentRequestId, orderNumber, accountId, paymentType, paymentFeature);
        paymentGateway.fail(paymentGatewayFailRequest);
    }
}
