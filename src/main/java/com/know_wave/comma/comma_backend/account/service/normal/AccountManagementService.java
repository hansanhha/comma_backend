package com.know_wave.comma.comma_backend.account.service.normal;

import com.know_wave.comma.comma_backend.account.dto.AccountResponse;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;

@Service
@Transactional
public class AccountManagementService {

    private final AccountQueryService accountQueryService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountManagementService(AccountQueryService accountQueryService, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountQueryService = accountQueryService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountResponse getAccount() {
        String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        return AccountResponse.of(account);
    }

    public boolean checkMatchPassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        account.setPassword(passwordEncoder.encode(password));
    }


    public void deleteAccount() {
        String accountId = getAuthenticatedId();
        Account account = accountQueryService.findAccount(accountId);

        accountRepository.delete(account);
    }


}
