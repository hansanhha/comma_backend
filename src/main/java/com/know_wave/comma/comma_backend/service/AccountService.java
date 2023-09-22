package com.know_wave.comma.comma_backend.service;

import com.know_wave.comma.comma_backend.entity.Account;
import com.know_wave.comma.comma_backend.entity.AccountVerify;
import com.know_wave.comma.comma_backend.repository.account.AccountRepository;
import com.know_wave.comma.comma_backend.repository.account.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.util.EmailSender;
import com.know_wave.comma.comma_backend.util.RandomUtils;
import com.know_wave.comma.comma_backend.web.dto.AccountCreateForm;
import com.know_wave.comma.comma_backend.web.exception.EmailNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AccountService {


    private final AccountRepository accountRepository;
    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSender emailSender;

    public AccountService(AccountRepository accountRepository, AccountVerifyRepository accountVerifyRepository, EmailSender emailSender) {
        this.accountRepository = accountRepository;
        this.accountVerifyRepository = accountVerifyRepository;
        this.emailSender = emailSender;
    }

    public String join(AccountCreateForm form) {
        Account newAccount = new Account(form.getId(), form.getEmail(), form.getPassword(), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        Account joinedAccount = accountRepository.save(newAccount);
        return joinedAccount.getId();
    }

    public Account getOne(String id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다"));
    }

    public void send(String email) {
        Optional<AccountVerify> byId = accountVerifyRepository.findById(email);
        AccountVerify findAccount = byId.orElse(null);

        int code = RandomUtils.sixDigitRandom();

        if (findAccount == null) {
            AccountVerify account = new AccountVerify(email, false, code);
            account.sendCode(emailSender);
            accountVerifyRepository.save(account);
            return;
        }

        findAccount.setCode(code);
        findAccount.sendCode(emailSender);
    }

    public boolean verify(String email, int code) {
        Optional<AccountVerify> byId = accountVerifyRepository.findById(email);
        AccountVerify findAccount = byId.orElseThrow(()->new EmailNotFoundException("요청된 이메일이 아닙니다"));

        boolean verify = findAccount.verifyCode(code);

        findAccount.setVerified(true);
        return verify;
    }
}
