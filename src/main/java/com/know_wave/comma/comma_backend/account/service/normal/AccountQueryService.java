package com.know_wave.comma.comma_backend.account.service.normal;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_AUTHENTICATED_REQUEST;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_EXIST_ACCOUNT;

@Service
@Transactional
public class AccountQueryService {

    private final AccountRepository accountRepository;

    public AccountQueryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(NOT_EXIST_ACCOUNT));
    }

    public static String getAuthenticatedId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name.isEmpty() || name.equals("anonymousUser")) {
            throw new IllegalArgumentException(NOT_AUTHENTICATED_REQUEST);
        }
        return name;
    }
}
