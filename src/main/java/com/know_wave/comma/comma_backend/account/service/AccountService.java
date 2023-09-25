package com.know_wave.comma.comma_backend.account.service;

import com.know_wave.comma.comma_backend.account.dto.*;
import com.know_wave.comma.comma_backend.account.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.account.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.AccountEmailVerify;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.account.repository.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.exception.EmailVerifiedException;
import com.know_wave.comma.comma_backend.exception.TokenNotFound;
import com.know_wave.comma.comma_backend.util.EmailSenderService;
import com.know_wave.comma.comma_backend.util.RandomUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.know_wave.comma.comma_backend.account.service.TokenService.*;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountVerifyRepository accountVerifyRepository;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, AccountVerifyRepository accountVerifyRepository, EmailSenderService emailSenderService, AuthenticationManager authenticationManager, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountVerifyRepository = accountVerifyRepository;
        this.emailSenderService = emailSenderService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public void join(AccountCreateForm form) {
        String email = form.getEmail();

        Optional<AccountEmailVerify> emailVerifyOptional = accountVerifyRepository.findById(email);

        emailVerifyOptional.ifPresentOrElse(
                accountEmailVerify -> {
                    if (!accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException("Not verified email");
                    }
                },
                () -> {
                    throw new NotFoundEmailException("Not verified email");
                }
        );

        Account account = new Account(form.getAccountId(), email, form.getName(), passwordEncoder.encode(form.getPassword()), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        accountRepository.save(account);
    }

    public void adminJoin(AdminCreateForm form) {
        Account account = new Account(form.getAccountId(), "none", form.getName(), passwordEncoder.encode(form.getPassword()), "9999999", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled, Role.ADMIN);
        accountRepository.save(account);
    }

    public Account getOne(String id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("NotExist Account"));
    }

    public void send(String email) {

        final int code = RandomUtils.generateRandomCode();
        Optional<AccountEmailVerify> accountEmailVerifyOptional = accountVerifyRepository.findById(email);

        accountEmailVerifyOptional.ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException("Existing verified account");
                    }

                    accountEmailVerify.setCode(code);
                    accountEmailVerify.sendCode(emailSenderService);
                },
                () -> {
                    AccountEmailVerify account = new AccountEmailVerify(email, false, code);
                    account.sendCode(emailSenderService);
                    accountVerifyRepository.save(account);
                }
        );
    }

    public boolean verifyEmail(String email, int code) {
        Optional<AccountEmailVerify> accountEmailVerifyOptional = accountVerifyRepository.findById(email);

        accountEmailVerifyOptional.ifPresentOrElse(accountEmailVerify ->
                {
                    if (accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException("Existing verified account");
                    }

                    if (accountEmailVerify.verifyCode(code)) {
                        accountEmailVerify.setVerified(true);
                    }
                },
                ()-> {
                    throw new NotFoundEmailException("NotExist Email");
                }
        );

        return accountEmailVerifyOptional.get().isVerified();
    }


    public Optional<TokenDto> processAuthentication(AccountSignInForm form) {

        Account account = findAccount(form.getAccountId());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getAccountId(), form.getPassword()));
        } catch (AuthenticationException e) {
            return Optional.empty();
        }

        UserDetails accountDetails = account.toUserDetails();

        final String accessToken = tokenService.generateAccessToken(accountDetails);
        final String refreshToken = tokenService.generateRefreshToken(accountDetails);

        tokenService.revokeAllTokens(accountDetails);
        tokenService.saveToken(refreshToken);

        return Optional.of(new TokenDto(accessToken, refreshToken));
    }

    public TokenDto refreshToken(String token) {

        Optional<Token> findOptionalToken = tokenService.findToken(token);
        Token findToken = findOptionalToken.orElseThrow(() -> new TokenNotFound(REFRESH_TOKEN_NOT_FOUND_MESSAGE));

        boolean validToken = tokenService.isValidToken(findToken);

        if (validToken) {
            Account account = findToken.getAccount();
            UserDetails accountDetails = account.toUserDetails();

            final String accessToken = tokenService.generateAccessToken(accountDetails);
            final String refreshToken = tokenService.generateRefreshToken(accountDetails);

            tokenService.revokeAllTokens(accountDetails);
            tokenService.saveToken(refreshToken);

            return new TokenDto(accessToken, refreshToken);
        }

        throw new ExpiredJwtException(null, null, REFRESH_TOKEN_INVALID_MESSAGE);
    }

    private Account findAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new EntityNotFoundException("NotExist Account"));
    }

    public AccountResponse getAccount() {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        return new AccountResponse(
                account.getId(),
                account.getEmail(),
                account.getAcademicNumber(),
                account.getMajor().name(),
                account.getAcademicStatus().getStatus(),
                account.getRole().getGrade()
        );
    }

    public boolean checkMatchPassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        return passwordEncoder.matches(password, account.getPassword());
    }

    public void changePassword(String password) {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        account.setPassword(passwordEncoder.encode(password));
    }

    private static String getAuthenticatedId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void deleteAccount() {
        String accountId = getAuthenticatedId();
        Account account = findAccount(accountId);

        accountRepository.delete(account);
    }
}
