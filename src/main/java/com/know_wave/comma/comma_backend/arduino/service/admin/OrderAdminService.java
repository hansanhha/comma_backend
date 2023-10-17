package com.know_wave.comma.comma_backend.arduino.service.admin;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.order.OrderResponse;
import com.know_wave.comma.comma_backend.arduino.entity.*;
import com.know_wave.comma.comma_backend.arduino.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoService;
import com.know_wave.comma.comma_backend.arduino.service.normal.OrderEmailService;
import com.know_wave.comma.comma_backend.arduino.service.normal.OrderQueryService;
import com.know_wave.comma.comma_backend.arduino.service.normal.OrderService;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import com.know_wave.comma.comma_backend.util.annotation.PermissionProtection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;
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
    private final OrderQueryService orderQueryService;

    public List<OrderResponse> getOrders(Pageable pageable) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllOrderByCreateDate(pageable);

        ValidateUtils.throwIfEmpty(orderInfos);

        return OrderResponse.ofList(orderInfos);
    }

    public List<List<OrderResponse>> getApplyOrders(Pageable pageable) {

    List<OrderResponse> mergedResponse;

    List<OrderInfo> orderInfos = orderInfoRepository.findAllApplyStatus(pageable);

    ValidateUtils.throwIfEmpty(orderInfos);

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
        OrderInfo orderInfo = orderQueryService.getOrderInfoById(orderNumber);

        List<ArduinoCategory> arduinoCategories = arduinoService.getArduinoCategoreisByArduinos(
                orderInfo.getOrders().stream().map(Order::getArduino).toList());

        Map<Arduino, List<ArduinoCategory>> arduinoCategoryMap = ArduinoCategory.groupingByArduino(arduinoCategories);

        return OrderDetailResponse.of(orderInfo, arduinoCategoryMap);
    }

    public List<OrderResponse> getOrdersByAccountId(String accountId) {
        Account account = accountQueryService.findAccount(accountId);
        List<OrderInfo> userOrderInfos = orderQueryService.getOrderInfosByAccount(account);

        return OrderResponse.ofList(userOrderInfos);
    }

    public List<OrderResponse> getCancelRequestOrders(Pageable pageable) {
        List<OrderInfo> cancelRequestOrderInfos = orderInfoRepository.findAllCancelRequestStatus(pageable);

        ValidateUtils.throwIfEmpty(cancelRequestOrderInfos);

        return OrderResponse.ofList(cancelRequestOrderInfos);
    }

    public void changeOrderStatus(String orderNumber, OrderStatus changedStatus) {
        OrderInfo orderInfo = orderQueryService.getOrderInfoById(orderNumber);

        if (orderInfo.getStatus().changeableTo(changedStatus)) {
            orderInfo.setStatus(changedStatus);

            Account account = accountQueryService.findAccount(getAuthenticatedId());
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
