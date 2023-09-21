package com.know_wave.comma.comma_backend.controller;

import com.know_wave.comma.comma_backend.service.AccountService;
import com.know_wave.comma.comma_backend.web.dto.AccountCreateForm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/account/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AccountCreateForm form) {
        accountService.join(form);
        return new ResponseEntity<>("계정 생성 완료", HttpStatus.CREATED);
    }
}
