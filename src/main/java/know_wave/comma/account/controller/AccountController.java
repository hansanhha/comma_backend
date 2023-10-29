package know_wave.comma.account.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import know_wave.comma.account.dto.*;
import know_wave.comma.account.service.auth.LogoutService;
import know_wave.comma.account.service.auth.SignService;
import know_wave.comma.account.service.normal.AccountManagementService;
import know_wave.comma.common.mail.EmailService;
import know_wave.comma.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final SignService signService;
    private final LogoutService logoutService;
    private final AccountManagementService accountManagementService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody AccountCreateForm form) {
        signService.join(form);
        return new ResponseEntity<>("Created account", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public Map<String, Object> signIn(@Valid @RequestBody AccountSignInForm form, HttpServletResponse response) {
        String accessToken = signService.processAuthentication(form);

        AddHeader(response, accessToken);

        return Map.of("msg", "Completed Authentication");
    }

    @GetMapping("/{accountId}/refresh-token")
    public ResponseEntity<String> refreshToken(@PathVariable("accountId") String accountId, HttpServletResponse response) {
        String accessToken = signService.refreshToken(accountId);

        AddHeader(response, accessToken);

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
    public Map<String, Object> deleteAccount(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        accountManagementService.deleteAccount();

        logoutService.logout(request, response, authentication);

        return Map.of("msg", "deleted account");
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

    private void AddHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(TokenService.AUTHORIZATION_HEADER, TokenService.TOKEN_PREFIX + accessToken);
    }
}
