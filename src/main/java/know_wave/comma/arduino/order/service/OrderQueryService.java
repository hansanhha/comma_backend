package know_wave.comma.arduino.order.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.order.dto.OrderDetailResponse;
import know_wave.comma.arduino.order.dto.OrderPageResponse;
import know_wave.comma.arduino.order.dto.OrderResponse;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AccountQueryService accountQueryService;

    public OrderPageResponse getOrderPage(Pageable pageable) {
        Account account = accountQueryService.findAccount();

        Page<Order> orders = orderRepository.findAllByAccount(account, pageable);
        List<OrderResponse> orderResponses = orders.stream().map(OrderResponse::to).toList();

        return OrderPageResponse.to(orderResponses,
                orders.isFirst(), orders.isLast(), orders.hasNext(), orders.getSize());
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findFetchByOrderNumber(orderNumber);

        if (optionalOrder.isEmpty()) {
            throw new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER);
        }

        Order order = optionalOrder.get();
        List<OrderDetail> orderDetails = orderDetailRepository.findFetchAllByOrder(order);

        return OrderDetailResponse.to(order, orderDetails);
    }

}
