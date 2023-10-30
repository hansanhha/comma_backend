package com.know_wave.comma.comma_backend.order.service.user;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.auth.Authority;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.entity.*;
import com.know_wave.comma.comma_backend.arduino.repository.BasketRepository;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoService;
import com.know_wave.comma.comma_backend.arduino.service.normal.BasketService;
import com.know_wave.comma.comma_backend.order.entity.Subject;
import com.know_wave.comma.comma_backend.order.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.order.dto.*;
import com.know_wave.comma.comma_backend.order.entity.Order;
import com.know_wave.comma.comma_backend.order.entity.OrderInfo;
import com.know_wave.comma.comma_backend.order.entity.OrderStatus;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final AccountQueryService accountQueryService;
    private final OrderInfoQueryService orderInfoQueryService;
    private final OrderEmailService orderEmailService;
    private final ArduinoService arduinoService;
    private final BasketService basketService;
    private final BasketRepository basketRepository;
    private final OrderInfoRepository orderInfoRepository;


    public void order(Subject subject) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(PERMISSION_DENIED);
        }

        List<Basket> baskets = basketService.getBasketsByAccount(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateUtils.generatedRandomCode();
        OrderInfo orderInfo = new OrderInfo(account, orderNumber, subject);

        Order.ofList(baskets, orderInfo);

        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);

        orderEmailService.sendOrderEmail(orderInfo);
    }

    public void order(String accountId, String orderNumber, Subject subject) {

        Account account = accountQueryService.findAccount(accountId);

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(PERMISSION_DENIED);
        }

        List<Basket> baskets = basketService.getBasketsByAccount(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        OrderInfo orderInfo = new OrderInfo(account, orderNumber, subject);

        Order.ofList(baskets, orderInfo);

        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);

        orderEmailService.sendOrderEmail(orderInfo);
    }

    public void moreOrder(String orderNumber, OrderMoreRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());

        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        if (orderInfo.isNotOrderer(account)) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }

        if (!orderInfo.getStatus().equals(OrderStatus.APPLIED)) {
            String message = OrderStatus.GET_ORDER_STATUS_MSG(orderInfo.getStatus());
            throw new IllegalArgumentException(message + " (주문번호 : " + orderNumber + ")");
        }

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(PERMISSION_DENIED);
        }

        if (arduino.isNotEnoughCount(request.getOrderCount())) {
            throw new IllegalArgumentException(NOT_ACCEPTABLE_REQUEST);
        }

        Order.of(orderInfo, arduino, request.getOrderCount());
    }

    public List<OrderResponse> getOrders() {

        final String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);
        List<OrderInfo> orderInfos = orderInfoQueryService.getOrderInfosByAccount(account);

        return OrderResponse.ofList(orderInfos);
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        if (orderInfo.isNotOrderer(account)) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }

        // 아누이노 목록과 연관된 카테고리 목록을 미리 가져오고 그룹화 (아두이노 1: 카테고리 N, N+1 문제 방지)
        List<ArduinoCategory> arduinoCategories = arduinoService.getArduinoCategoreisByArduinos(
                                                            orderInfo.getOrders().stream().map(Order::getArduino).toList());

        Map<Arduino, List<ArduinoCategory>> arduinoCategoryMap = ArduinoCategory.groupingByArduino(arduinoCategories);

        return OrderDetailResponse.of(orderInfo, arduinoCategoryMap);
    }

    public void cancelOrderRequest(String orderNumber, OrderCancelRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());

        orderInfoRepository.findById(orderNumber).ifPresentOrElse(orderInfo -> {

                if (orderInfo.isNotOrderer(account)) {
                    throw new BadCredentialsException(BAD_CREDENTIALS);
                }

                if (!orderInfo.isCancellable()) {
                    String message = OrderStatus.GET_ORDER_STATUS_MSG(orderInfo.getStatus());
                    throw new IllegalArgumentException(message + " (주문번호 : " + orderNumber + ")");
                }

                orderInfo.setStatus(OrderStatus.CANCELLATION_REQUEST);
                orderInfo.setCancellationReason(request.getReason());
            },
            () -> {
                throw new EntityNotFoundException(NOT_FOUND_VALUE);
            }
        );
    }



}
