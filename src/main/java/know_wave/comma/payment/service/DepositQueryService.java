package know_wave.comma.payment.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.payment.entity.Deposit;
import know_wave.comma.payment.repository.DepositRepository;
import know_wave.comma.common.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositQueryService {

    private final DepositRepository depositRepository;

    public Deposit getDepositByRequestId(String paymentRequestId) {
        Optional<Deposit> depositOptional = depositRepository.fetchAccountByPaymentRequestId(paymentRequestId);

        ValidateUtils.throwIfEmpty(depositOptional);

        return depositOptional.get();
    }

    public Deposit getDepositById(Long id) {
        Optional<Deposit> depositOptional = depositRepository.findById(id);

        ValidateUtils.throwIfEmpty(depositOptional);

        return depositOptional.get();
    }

    public List<Deposit> getDeposits(Account account, OrderInfo orderInfo) {
        List<Deposit> deposits = depositRepository.findAllByAccountAndOrderInfo(account, orderInfo);

        return deposits;
    }
}
