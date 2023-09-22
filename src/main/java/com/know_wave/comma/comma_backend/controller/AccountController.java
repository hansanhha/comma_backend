package com.know_wave.comma.comma_backend.controller;

import com.know_wave.comma.comma_backend.service.AccountService;
import com.know_wave.comma.comma_backend.web.dto.AccountCreateForm;
import com.know_wave.comma.comma_backend.web.dto.EmailAuthRequestDto;
import com.know_wave.comma.comma_backend.web.dto.EmailVerifyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AccountCreateForm form) {
        accountService.join(form);
        return new ResponseEntity<>("계정 생성 완료", HttpStatus.CREATED);
    }

    @PostMapping("/email/r")
    public ResponseEntity<String> emailAuthenticationRequest(@Valid @RequestBody EmailAuthRequestDto requestDto) {
        accountService.send(requestDto.getEmail());
        return new ResponseEntity<>("인증 요청 이메일 전송 완료", HttpStatus.OK);
    }

    @PostMapping("/email/verify")
    public ResponseEntity emailAuthentication(@Valid @RequestBody EmailVerifyDto requestDto) {
        boolean verify = accountService.verify(requestDto.getEmail(), requestDto.getCode());
        if (verify) return new ResponseEntity<>("학교 이메일 인증 완료", HttpStatus.OK);
        else return new ResponseEntity<>("학교 이메일 인증 실패", HttpStatus.BAD_REQUEST);
    }

}
