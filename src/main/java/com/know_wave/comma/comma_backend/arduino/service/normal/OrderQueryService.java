package com.know_wave.comma.comma_backend.arduino.service.normal;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.arduino.repository.OrderInfoRepository;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_FOUND_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo fetchOrdersArduinoAccount(String orderNumber) {
        return orderInfoRepository.findFetchOrdersArduinoAccountById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));
    }

    public OrderInfo fetchAccount(String orderNumber) {
        return orderInfoRepository.findFetchAccountById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_VALUE));
    }

    public List<OrderInfo> getOrderInfosByAccount(Account account) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(orderInfos);
        return orderInfos;
    }
}
