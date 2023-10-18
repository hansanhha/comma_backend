package com.know_wave.comma.comma_backend.account.service.admin;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource;
import com.know_wave.comma.comma_backend.security.annotation.PermissionProtection;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@PermissionProtection
public class AccountAdminService {

    private final AccountRepository accountRepository;

    public AccountAdminService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void changeAccountRole(Role role, String specifiedId) {
        Account account = findAccount(specifiedId);
        account.setRole(role);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessageSource.NOT_EXIST_ACCOUNT));
    }
}
