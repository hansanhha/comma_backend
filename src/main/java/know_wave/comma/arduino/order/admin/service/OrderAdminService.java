package know_wave.comma.arduino.order.admin.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.notification.dto.OrderNotificationRequest;
import know_wave.comma.arduino.notification.service.ArduinoOrderNotification;
import know_wave.comma.arduino.order.admin.dto.AdminOrderDetailResponse;
import know_wave.comma.arduino.order.admin.dto.AdminOrderPageResponse;
import know_wave.comma.arduino.order.admin.dto.AdminOrderStatusResponse;
import know_wave.comma.arduino.order.admin.dto.OrderStatusUpdateRequest;
import know_wave.comma.arduino.order.admin.exception.AdminOrderException;
import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import know_wave.comma.arduino.order.entity.OrderStatus;
import know_wave.comma.arduino.order.exception.UnableOrderUpdateStatus;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.service.PushNotificationGateway;
import know_wave.comma.payment.dto.gateway.PaymentGatewayRefundResponse;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentStatus;
import know_wave.comma.payment.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderAdminService {

    private final AccountQueryService accountQueryService;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ArduinoOrderNotification orderNotification;

    public AdminOrderPageResponse getOrderedPage(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByOrderStatus(OrderStatus.ORDERED, pageable);

        return AdminOrderPageResponse.to(orderPage);
    }

    public AdminOrderPageResponse getOrderPageByAccount(Pageable pageable) {
        Account account = accountQueryService.findAccount();

        Page<Order> orderPage = orderRepository.findAllByAccount(account, pageable);

        return AdminOrderPageResponse.to(orderPage);
    }

    public AdminOrderPageResponse getAllOrderPage(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        return AdminOrderPageResponse.to(orderPage);
    }

    public AdminOrderDetailResponse getOrderDetail(String orderNumber) {
        Order order = getOrder(orderNumber);

        List<OrderDetail> orderDetails = orderDetailRepository.findFetchAllByOrder(order);

        return AdminOrderDetailResponse.to(order, orderDetails);
    }

    public AdminOrderStatusResponse updateOrderStatus(String orderNumber, OrderStatusUpdateRequest updateRequest) {
        Order order = getOrder(orderNumber);
        String orderStatus = updateRequest.getOrderStatus();

        AdminOrderStatusResponse response = switch (orderStatus) {
            case "reject" -> rejectOrder(order);
            case "prepare" -> prepareOrder(order);
            case "beReady" -> beReadyOrder(order);
            case "received" -> receivedOrder(order);
            default -> throw new UnableOrderUpdateStatus(ExceptionMessageSource.INVALID_ORDER_STATUS);
        };

        OrderNotificationRequest orderNotificationRequest =
                OrderNotificationRequest.create(
                        order.getOrderStatus(), order.getDeposit().getDepositStatus(),
                        orderNumber, order.getAccount().getId());

        orderNotification.notify(orderNotificationRequest);

        return response;
    }

    private AdminOrderStatusResponse rejectOrder(Order order) {
        String beforeOrderStatus = order.getOrderStatus().getStatus();

        order.rejectOrder();
        String afterOrderStatus = order.getOrderStatus().getStatus();
        String depositStatus = order.getDeposit().getDepositStatus().getStatus();

        Payment payment = order.getDeposit().getPayment();

        if (!payment.isPaymentComplete()) {
            throw new AdminOrderException(ExceptionMessageSource.UNABLE_TO_REJECT_ORDER);
        }

        String paymentRequestId = payment.getPaymentRequestId();
        PaymentGatewayRefundResponse refund = paymentGateway.refund(paymentRequestId);

        if (refund.getPaymentStatus() != PaymentStatus.REFUND) {
            throw new AdminOrderException(ExceptionMessageSource.FAILED_DEPOSIT_RETURN);
        }

        return AdminOrderStatusResponse
                .create(order.getOrderNumber(), beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    private AdminOrderStatusResponse prepareOrder(Order order) {
        String beforeOrderStatus = order.getOrderStatus().getStatus();

        order.prepareOrder();

        String afterOrderStatus = order.getOrderStatus().getStatus();
        String depositStatus = order.getDeposit().getDepositStatus().getStatus();

        return AdminOrderStatusResponse
                .create(order.getOrderNumber(), beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    private AdminOrderStatusResponse beReadyOrder(Order order) {
        String beforeOrderStatus = order.getOrderStatus().getStatus();

        order.beReadyOrder();

        String afterOrderStatus = order.getOrderStatus().getStatus();
        String depositStatus = order.getDeposit().getDepositStatus().getStatus();

        return AdminOrderStatusResponse
                .create(order.getOrderNumber(), beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    private AdminOrderStatusResponse receivedOrder(Order order) {
        String beforeOrderStatus = order.getOrderStatus().getStatus();

        order.receiveOrder();

        String afterOrderStatus = order.getOrderStatus().getStatus();
        String depositStatus = order.getDeposit().getDepositStatus().getStatus();

        Payment payment = order.getDeposit().getPayment();
        String paymentRequestId = payment.getPaymentRequestId();
        PaymentGatewayRefundResponse refund = paymentGateway.refund(paymentRequestId);

        if (refund.getPaymentStatus() != PaymentStatus.REFUND) {
            throw new AdminOrderException(ExceptionMessageSource.FAILED_DEPOSIT_RETURN);
        }

        return AdminOrderStatusResponse
                .create(order.getOrderNumber(), beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    private Order getOrder(String orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findFetchAccountAndOrderNumber(orderNumber);

        if (optionalOrder.isEmpty()) {
            throw new AdminOrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER);
        }
        return optionalOrder.get();
    }
}
