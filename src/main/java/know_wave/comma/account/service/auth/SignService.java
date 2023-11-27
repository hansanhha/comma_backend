package know_wave.comma.account.service.auth;

import know_wave.comma.account.dto.AccountCreateForm;
import know_wave.comma.account.dto.AccountSignInForm;
import know_wave.comma.account.dto.AdminCreateForm;
import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.AcademicStatus;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.entity.auth.Role;
import know_wave.comma.account.exception.SignInFailureException;
import know_wave.comma.account.repository.AccountRepository;
import know_wave.comma.account.repository.AccountVerifyRepository;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.common.mail.exception.EmailVerifiedException;
import know_wave.comma.common.mail.exception.NotFoundEmailException;
import know_wave.comma.common.config.security.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import know_wave.comma.common.message.ExceptionMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        throw new EmailVerifiedException(ExceptionMessageSource.NOT_VERIFIED_EMAIL);
                    }
                },
                () -> {
                    throw new NotFoundEmailException(ExceptionMessageSource.NOT_VERIFIED_EMAIL);
                });

        Account account = new Account(form.getAccountId(), email, form.getName(), passwordEncoder.encode(form.getPassword()), form.getAcademicNumber(), form.getMajor(), form.getStatus());
        accountRepository.save(account);
    }

    public void adminJoin(AdminCreateForm form) {
        Account account = new Account(form.getAccountId(), "none", form.getName(), passwordEncoder.encode(form.getPassword()), "9999999", AcademicMajor.SoftwareEngineering, AcademicStatus.Enrolled, Role.ADMIN);
        accountRepository.save(account);
    }

    public String processAuthentication(AccountSignInForm form) {

        Account account = accountQueryService.findAccount(form.getAccountId());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getAccountId(), form.getPassword()));
        } catch (AuthenticationException e) {
            throw new SignInFailureException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        UserDetails accountDetails = account.toUserDetails();

        final String accessToken = tokenService.generateAccessToken(accountDetails);
        final String refreshToken = tokenService.generateRefreshToken(accountDetails);

        tokenService.revokeAllRefreshTokens(account);
        tokenService.saveToken(refreshToken);

        return accessToken;
    }

    public String refreshToken(String accountId) {

        Account account = accountQueryService.findAccount(accountId);

        tokenService.checkRefreshToken(account);
        boolean existValidRefreshToken = tokenService.existValidRefreshToken(account);

        if (existValidRefreshToken) {
            tokenService.revokeAllRefreshTokens(account);
            return tokenService.generateAccessToken(account.toUserDetails());
        } else {
            throw new ExpiredJwtException(null, null, "Refresh Token이 만료되었습니다. 재로그인을 해주세요.");
        }
    }
}
