package com.know_wave.comma.comma_backend.account.service.auth;

import com.know_wave.comma.comma_backend.account.dto.AccountCreateForm;
import com.know_wave.comma.comma_backend.account.dto.AccountSignInForm;
import com.know_wave.comma.comma_backend.account.dto.AdminCreateForm;
import com.know_wave.comma.comma_backend.account.dto.TokenDto;
import com.know_wave.comma.comma_backend.account.entity.AcademicMajor;
import com.know_wave.comma.comma_backend.account.entity.AcademicStatus;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.auth.Role;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.repository.AccountRepository;
import com.know_wave.comma.comma_backend.account.repository.AccountVerifyRepository;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.util.exception.EmailVerifiedException;
import com.know_wave.comma.comma_backend.util.exception.NotFoundEmailException;
import com.know_wave.comma.comma_backend.util.exception.TokenNotFound;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
public class SignService {

    private final AccountVerifyRepository accountVerifyRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountQueryService accountQueryService;

    public SignService(AccountVerifyRepository accountVerifyRepository, AuthenticationManager authenticationManager, TokenService tokenService, AccountRepository accountRepository, PasswordEncoder passwordEncoder, AccountQueryService accountQueryService) {
        this.accountVerifyRepository = accountVerifyRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountQueryService = accountQueryService;
    }

    public void join(AccountCreateForm form) {
        String email = form.getEmail();

        accountVerifyRepository.findById(email).ifPresentOrElse(
                accountEmailVerify -> {
                    if (!accountEmailVerify.isVerified()) {
                        throw new EmailVerifiedException(NOT_VERIFIED_EMAIL);
                    }
                },
                () -> {
                    throw new NotFoundEmailException(NOT_VERIFIED_EMAIL);
                });

        Account account = new Account(form.getAccountId(), email, form.getName(), passwordEncoder.encode(form.getPassword()), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        accountRepository.save(account);
    }

    public void adminJoin(AdminCreateForm form) {
        Account account = new Account(form.getAccountId(), "none", form.getName(), passwordEncoder.encode(form.getPassword()), "9999999", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled, Role.ADMIN);
        accountRepository.save(account);
    }

    public Optional<TokenDto> processAuthentication(AccountSignInForm form) {

        Account account = accountQueryService.findAccount(form.getAccountId());

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
        Token findToken = findOptionalToken.orElseThrow(() -> new TokenNotFound(NOT_FOUND_TOKEN));

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

        throw new ExpiredJwtException(null, null, INVALID_TOKEN);
    }
}
