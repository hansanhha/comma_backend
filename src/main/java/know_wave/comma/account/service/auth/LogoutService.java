package know_wave.comma.account.service.auth;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;
    private final AccountQueryService accountQueryService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accountId = (String) authentication.getPrincipal();

        if (!accountId.isEmpty()) {
            Account account = accountQueryService.findAccount(accountId);
            tokenService.revokeAllRefreshTokens(account);
        }
    }
}
