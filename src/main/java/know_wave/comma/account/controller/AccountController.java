package know_wave.comma.account.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import know_wave.comma.account.dto.*;
import know_wave.comma.account.service.AccountManagementService;
import know_wave.comma.account.service.SignUpService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.dto.AccountSignInForm;
import know_wave.comma.config.security.dto.SignInResponse;
import know_wave.comma.config.security.exception.NotFoundTokenException;
import know_wave.comma.config.security.service.JwtLogoutHandler;
import know_wave.comma.config.security.service.JwtSignInHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static know_wave.comma.config.security.filter.JwtAuthenticationFilter.TOKEN_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final SignUpService signUpService;
    private final JwtSignInHandler signInService;
    private final JwtLogoutHandler logoutService;
    private final AccountManagementService accountManagementService;
    private static final String MESSAGE = "msg";
    private static final String DATA = "body";
    private static final String REFRESH_TOKEN = "refreshToken";

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody AccountCreateForm form) {
        signUpService.join(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE,"Created account"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signIn(@Valid @RequestBody AccountSignInForm form) {
        SignInResponse signInResponse = signInService.signIn(form.getAccountId(), form.getPassword());

        Map<String, Object> body = Map.of(MESSAGE, "authenticated", DATA, signInResponse);

        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/refresh-token")
    public Map<String, String> refreshToken(HttpEntity<String> httpEntity) {
        List<String> authorizations = httpEntity.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authorizations == null || authorizations.isEmpty()) {
            throw new NotFoundTokenException(ExceptionMessageSource.NOT_FOUND_TOKEN);
        }

        String refreshToken = authorizations.getFirst().substring(TOKEN_PREFIX.length());
        String accessToken = signInService.reissueAccessToken(refreshToken);

        return Map.of(MESSAGE, "reissued access token", DATA, accessToken);
    }

    @PostMapping(path = "/email/verify/request")
    public ResponseEntity<Map<String, String>> sendEmailVerifyCode(@Valid @RequestBody EmailSendRequest emailRequest) {
        signUpService.sendEmailVerifyCode(emailRequest.getEmail());
        return ResponseEntity.ok().body(Map.of(MESSAGE, "sent email code"));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Map<String, String>> emailVerify(@Valid @RequestBody EmailVerifyRequest requestDto) {
        boolean result = signUpService.verifyEmailCode(requestDto.getEmail(), Integer.parseInt(requestDto.getCode()));
        if (result) return ResponseEntity.ok(Map.of(MESSAGE, "authenticated email"));
        else return new ResponseEntity<>(Map.of(MESSAGE, "failed authentication email"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/me")
    public AccountResponse getAccount() {
        return accountManagementService.getAccount();
    }

    @DeleteMapping("/me")
    public Map<String, Object> deleteAccount(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        accountManagementService.deleteAccount();

        logoutService.logout(request, response, authentication);

        return Map.of(MESSAGE, "deleted account");
    }

    @PostMapping("/password/check")
    public ResponseEntity<Map<String, Boolean>> checkPassword(@Valid @RequestBody AccountPasswordChangeRequest changeRequest) {
        boolean isSame = accountManagementService.checkMatchPassword(changeRequest.getPassword());

        if (isSame) {
            return ResponseEntity.ok(Map.of(DATA, true));
        }

        return new ResponseEntity<>(Map.of(DATA,false), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/password")
    public Map<String, String> changePassword(@Valid @RequestBody AccountPasswordChangeRequest changeRequest) {
        accountManagementService.changePassword(changeRequest.getPassword());
        return Map.of(MESSAGE, "changed password");
    }

//    private void AddHeader(HttpServletResponse response, String accessToken) {
//        response.addHeader(JwtTokenService.AUTHORIZATION_HEADER, JwtTokenService.TOKEN_PREFIX + accessToken);
//    }
}
