package com.know_wave.comma.comma_backend.service;

import com.know_wave.comma.comma_backend.entity.Account;
import com.know_wave.comma.comma_backend.repository.account.AccountRepository;
import com.know_wave.comma.comma_backend.web.dto.AccountCreateForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public String join(AccountCreateForm form) {
        Account newAccount = new Account(form.getId(), form.getEmail(), form.getPassword(), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        Account joinedAccount = accountRepository.save(newAccount);
        return joinedAccount.getId();
    }

    public Account getOne(String id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다"));
    }
}
