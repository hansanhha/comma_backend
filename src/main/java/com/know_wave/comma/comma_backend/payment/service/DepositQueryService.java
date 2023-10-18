package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.repository.DepositRepository;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositQueryService {

    private final DepositRepository depositRepository;

    public Deposit getDeposit(String paymentRequestId) {
        Optional<Deposit> depositOptional = depositRepository.findByPaymentRequestId(paymentRequestId);

        ValidateUtils.throwIfEmpty(depositOptional);

        return depositOptional.get();
    }
}
