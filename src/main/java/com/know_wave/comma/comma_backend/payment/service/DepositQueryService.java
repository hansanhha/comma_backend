package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.repository.DepositRepository;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
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
        Optional<Deposit> depositOptional = depositRepository.findByPaymentRequestId(paymentRequestId);

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
