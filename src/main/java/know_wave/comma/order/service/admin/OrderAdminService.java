package know_wave.comma.order.service.admin;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.auth.Role;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.ArduinoCategory;
import know_wave.comma.arduino.service.normal.ArduinoService;
import know_wave.comma.common.config.security.annotation.PermissionProtection;
import know_wave.comma.order.dto.OrderDetailResponse;
import know_wave.comma.order.dto.OrderResponse;
import know_wave.comma.order.entity.Order;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.order.entity.OrderStatus;
import know_wave.comma.order.repository.OrderInfoRepository;
import know_wave.comma.order.service.user.OrderEmailService;
import know_wave.comma.order.service.user.OrderInfoQueryService;
import know_wave.comma.order.service.user.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Service
@Transactional
@PermissionProtection
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderEmailService orderEmailService;
    private final OrderService orderService;
    private final ArduinoService arduinoService;
    private final AccountQueryService accountQueryService;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderInfoQueryService orderInfoQueryService;

    public List<OrderResponse> getOrders(Pageable pageable) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllOrderByCreateDate(pageable);

        return OrderResponse.ofList(orderInfos);
    }

    public List<List<OrderResponse>> getApplyOrders(Pageable pageable) {

    List<OrderResponse> mergedResponse;

    List<OrderInfo> orderInfos = orderInfoRepository.findAllApplyStatus(pageable);

    if (orderInfos.isEmpty()) {
        return List.of();
    }

    List<OrderInfo> relatedOrderInfo = orderInfoRepository.findAllApplyStatusByRelatedAccount(
            orderInfos.stream().map(OrderInfo::getAccount).collect(toUnmodifiableSet()),
            orderInfos.stream().map(OrderInfo::getOrderNumber).toList()
    );

    List<OrderResponse> orderResponses = orderInfos.stream().map(OrderResponse::of).toList();
    List<OrderResponse> relatedResponse = relatedOrderInfo.stream().map(OrderResponse::of).toList();

    if (relatedResponse.isEmpty()) {
        mergedResponse = orderResponses;
    } else {
        mergedResponse = Stream.concat(orderResponses.stream(), relatedResponse.stream()).toList();
    }

    Map<String, List<OrderResponse>> groupedOrderResponses = OrderResponse.groupingOrderByAccountId(mergedResponse);

    return OrderResponse.ofGroupList(groupedOrderResponses);
    }

    public OrderDetailResponse getOrderDetailByOrderNumber(String orderNumber) {
        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        List<ArduinoCategory> arduinoCategories = arduinoService.getArduinoCategoreisByArduinos(
                orderInfo.getOrders().stream().map(Order::getArduino).toList());

        Map<Arduino, List<ArduinoCategory>> arduinoCategoryMap = ArduinoCategory.groupingByArduino(arduinoCategories);

        return OrderDetailResponse.of(orderInfo, arduinoCategoryMap);
    }

    public List<OrderResponse> getOrdersByAccountId(String accountId) {
        Account account = accountQueryService.findAccount(accountId);
        List<OrderInfo> userOrderInfos = orderInfoQueryService.getOrderInfosByAccount(account);

        return OrderResponse.ofList(userOrderInfos);
    }

    public List<OrderResponse> getCancelRequestOrders(Pageable pageable) {
        List<OrderInfo> cancelRequestOrderInfos = orderInfoRepository.findAllCancelRequestStatus(pageable);

        return OrderResponse.ofList(cancelRequestOrderInfos);
    }

    public void changeOrderStatus(String orderNumber, OrderStatus changedStatus) {
        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        if (orderInfo.getStatus().changeableTo(changedStatus)) {
            orderInfo.setStatus(changedStatus);

            Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
            if (account.getRole() != Role.ADMIN) { // 관리자 계정은 이메일이 없으므로 메일 전송 X
                orderEmailService.sendOrderEmail(orderInfo);
            }
        } else {
            String message = OrderStatus.GET_ORDER_STATUS_MSG(orderInfo.getStatus());
            String message2 = OrderStatus.GET_IMMUTABLE_ORDER_STATUS_MSG(changedStatus);
            throw new IllegalArgumentException(message + " " + message2 + " (주문번호 : " + orderNumber + ")");
        }
    }

    public void changeOrdersStatus(List<String> OrderNumbers, OrderStatus changedStatus) {
        OrderNumbers.forEach(orderNumber -> changeOrderStatus(orderNumber, changedStatus));
    }
}
