package know_wave.comma.order.service.user;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.order.repository.OrderInfoRepository;
import know_wave.comma.common.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.common.message.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderInfoQueryService {

    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo fetchOrdersArduinoAccount(String orderNumber) {
        return orderInfoRepository.findFetchOrdersArduinoAccountById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

    public OrderInfo fetchAccount(String orderNumber) {
        return orderInfoRepository.findFetchAccountById(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

    public Optional<OrderInfo> optionalFetchAccount(String orderNumber) {
        return orderInfoRepository.findFetchAccountById(orderNumber);// optionalF
    }

    public List<OrderInfo> getOrderInfosByAccount(Account account) {
        List<OrderInfo> orderInfos = orderInfoRepository.findAllByAccount(account);

        ValidateUtils.throwIfEmpty(orderInfos);
        return orderInfos;
    }
}
