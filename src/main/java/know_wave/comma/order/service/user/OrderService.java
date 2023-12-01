package know_wave.comma.order.service.user;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.auth.Authority;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.ArduinoCategory;
import know_wave.comma.arduino.entity.Basket;
import know_wave.comma.arduino.repository.BasketRepository;
import know_wave.comma.arduino.service.normal.ArduinoService;
import know_wave.comma.arduino.service.normal.BasketService;
import know_wave.comma.alarm.util.ExceptionMessageSource;
import know_wave.comma.common.util.GenerateUtils;
import know_wave.comma.order.dto.OrderCancelRequest;
import know_wave.comma.order.dto.OrderDetailResponse;
import know_wave.comma.order.dto.OrderMoreRequest;
import know_wave.comma.order.dto.OrderResponse;
import know_wave.comma.order.entity.Order;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.order.entity.OrderStatus;
import know_wave.comma.order.entity.Subject;
import know_wave.comma.order.repository.OrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(ExceptionMessageSource.PERMISSION_DENIED);
        }

        List<Basket> baskets = basketService.getBasketsByAccount(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_ACCEPTABLE_REQUEST);
        }

        String orderNumber = GenerateUtils.generateRandomCode();
        OrderInfo orderInfo = new OrderInfo(account, orderNumber, subject);

        Order.ofList(baskets, orderInfo);

        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);

        orderEmailService.sendOrderEmail(orderInfo);
    }

    public void order(String accountId, String orderNumber, Subject subject) {

        Account account = accountQueryService.findAccount(accountId);

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(ExceptionMessageSource.PERMISSION_DENIED);
        }

        List<Basket> baskets = basketService.getBasketsByAccount(account);

        if (Basket.isOverRequest(baskets) || Basket.isEmpty(baskets)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_ACCEPTABLE_REQUEST);
        }

        OrderInfo orderInfo = new OrderInfo(account, orderNumber, subject);

        Order.ofList(baskets, orderInfo);

        orderInfoRepository.save(orderInfo);
        basketRepository.deleteAll(baskets);

        orderEmailService.sendOrderEmail(orderInfo);
    }

    public void moreOrder(String orderNumber, OrderMoreRequest request) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        Arduino arduino = arduinoService.getArduino(request.getArduinoId());

        if (orderInfo.isNotOrderer(account)) {
            throw new BadCredentialsException(ExceptionMessageSource.BAD_CREDENTIALS);
        }

        if (!orderInfo.getStatus().equals(OrderStatus.APPLIED)) {
            String message = OrderStatus.GET_ORDER_STATUS_MSG(orderInfo.getStatus());
            throw new IllegalArgumentException(message + " (주문번호 : " + orderNumber + ")");
        }

        if (account.dontHaveAuthority(Authority.MEMBER_EQUIPMENT_APPLY)) {
            throw new AccessDeniedException(ExceptionMessageSource.PERMISSION_DENIED);
        }

        if (arduino.isNotEnoughCount(request.getOrderCount())) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_ACCEPTABLE_REQUEST);
        }

        Order.of(orderInfo, arduino, request.getOrderCount());
    }

    public List<OrderResponse> getOrders() {

        final String accountId = accountQueryService.getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);
        List<OrderInfo> orderInfos = orderInfoQueryService.getOrderInfosByAccount(account);

        return OrderResponse.ofList(orderInfos);
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        OrderInfo orderInfo = orderInfoQueryService.fetchOrdersArduinoAccount(orderNumber);

        if (orderInfo.isNotOrderer(account)) {
            throw new BadCredentialsException(ExceptionMessageSource.BAD_CREDENTIALS);
        }

        // 아누이노 목록과 연관된 카테고리 목록을 미리 가져오고 그룹화 (아두이노 1: 카테고리 N, N+1 문제 방지)
        List<ArduinoCategory> arduinoCategories = arduinoService.getArduinoCategoreisByArduinos(
                                                            orderInfo.getOrders().stream().map(Order::getArduino).toList());

        Map<Arduino, List<ArduinoCategory>> arduinoCategoryMap = ArduinoCategory.groupingByArduino(arduinoCategories);

        return OrderDetailResponse.of(orderInfo, arduinoCategoryMap);
    }

    public void cancelOrderRequest(String orderNumber, OrderCancelRequest request) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

        orderInfoRepository.findById(orderNumber).ifPresentOrElse(orderInfo -> {

                if (orderInfo.isNotOrderer(account)) {
                    throw new BadCredentialsException(ExceptionMessageSource.BAD_CREDENTIALS);
                }

                if (!orderInfo.isCancellable()) {
                    String message = OrderStatus.GET_ORDER_STATUS_MSG(orderInfo.getStatus());
                    throw new IllegalArgumentException(message + " (주문번호 : " + orderNumber + ")");
                }

                orderInfo.setStatus(OrderStatus.CANCELLATION_REQUEST);
                orderInfo.setCancellationReason(request.getReason());
            },
            () -> {
                throw new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE);
            }
        );
    }



}
