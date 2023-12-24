package know_wave.comma.config.security.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenHandler jwtTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Object principal = authentication.getPrincipal();

        // principal 설정 방식에 따라 다르게 처리
        // (AbstractUserDetailsAuthenticationProvider - forcePrincipalAsString 참고)
        if (principal instanceof UserDetails userDetails) {
            jwtTokenService.revokeRefreshToken(userDetails.getUsername());
        } else {
            jwtTokenService.revokeRefreshToken((String) principal);
        }
    }
}
