package know_wave.comma.order_.service.user;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.alarm.util.ExceptionMessageSource;
import know_wave.comma.order_.entity.OrderInfo;
import know_wave.comma.order_.repository.OrderInfoRepository;
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
        return orderInfoRepository.findAllByAccount(account);
    }
}
