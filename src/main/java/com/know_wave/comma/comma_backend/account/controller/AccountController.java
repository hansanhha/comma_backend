package com.know_wave.comma.comma_backend.account.controller;

import com.know_wave.comma.comma_backend.account.dto.*;
import com.know_wave.comma.comma_backend.account.service.normal.AccountManagementService;
import com.know_wave.comma.comma_backend.common.mail.EmailService;
import com.know_wave.comma.comma_backend.account.service.auth.SignService;
import com.know_wave.comma.comma_backend.account.service.auth.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final SignService signService;
    private final AccountManagementService accountManagementService;
    private final EmailService emailService;

    @Value("${REFRESH_TOKEN_COOKIE_EXPIRATION}")
    private int refreshTokenExpiration;
    @Value("${ACCESS_TOKEN_COOKIE_EXPIRATION}")
    private int accessTokenExpiration;

    public AccountController(SignService signService, AccountManagementService accountManagementService, EmailService emailService) {
        this.signService = signService;
        this.accountManagementService = accountManagementService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AccountCreateForm form) {
        signService.join(form);
        return new ResponseEntity<>("Created account", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody AccountSignInForm form, HttpServletResponse response) {
        Optional<TokenDto> tokenDtoOptional = signService.processAuthentication(form);

        if (tokenDtoOptional.isEmpty()) {
            return new ResponseEntity<>("Failed Authentication", HttpStatus.UNAUTHORIZED);
        }

        TokenDto tokenDto = tokenDtoOptional.get();
        response.addCookie(getCookie(TokenService.ACCESS_TOKEN_NAME, tokenDto.getAccessToken(), accessTokenExpiration));
        response.addCookie(getCookie(TokenService.REFRESH_TOKEN_NAME, tokenDto.getRefreshToken(), refreshTokenExpiration));

        return ResponseEntity.ok("Completed authentication");
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@CookieValue(value = "refresh_token") String refreshToken, HttpServletResponse response) {
        TokenDto tokenDto = signService.refreshToken(refreshToken);

        response.addCookie(getCookie(TokenService.ACCESS_TOKEN_NAME, tokenDto.getAccessToken(), accessTokenExpiration));
        response.addCookie(getCookie(TokenService.REFRESH_TOKEN_NAME, tokenDto.getRefreshToken(), refreshTokenExpiration));

        return ResponseEntity.ok("Completed Issue tokens");
    }

    @PostMapping("/email/r")
    public ResponseEntity<String> emailAuthenticationRequest(@Valid @RequestBody EmailAuthRequest requestDto) {
        emailService.sendAuthCode(requestDto.getEmail());
        return ResponseEntity.ok("Send authentication code email");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<String> emailAuthentication(@Valid @RequestBody EmailVerifyRequest requestDto) {
        boolean result = emailService.verifyAuthCode(requestDto.getEmail(), Integer.parseInt(requestDto.getCode()));
        if (result) return ResponseEntity.ok("Completed email authentication");
        else return new ResponseEntity<>("Failed email authentication", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getAccount() {
        AccountResponse accountResponse = accountManagementService.getAccount();
        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteAccount(HttpServletResponse response) {
        accountManagementService.deleteAccount();
        removeAllCookies(response);
        return ResponseEntity.ok("Completed delete account");
    }


    @PostMapping("/password")
    public ResponseEntity<String> checkPassword(@Valid @RequestBody AccountPasswordRequest requestDto) {
        boolean isSame = accountManagementService.checkMatchPassword(requestDto.getPassword());

        if (!isSame) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok("Correct password");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody AccountPasswordRequest requestDto) {
        accountManagementService.changePassword(requestDto.getPassword());
        return ResponseEntity.ok("Completed change password");
    }

    private static Cookie getCookie(String tokenName, String token, int expiration) {
        Cookie cookie = new Cookie(tokenName, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(expiration);
//        cookie.setSecure(true);
        return cookie;
    }

    private void removeAllCookies(HttpServletResponse response) {
        response.addCookie(getCookie(TokenService.ACCESS_TOKEN_NAME, null, 0));
        response.addCookie(getCookie(TokenService.REFRESH_TOKEN_NAME, null, 0));
    }
}
