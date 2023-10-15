package com.know_wave.comma.comma_backend.account.service.auth;

import com.know_wave.comma.comma_backend.account.dto.TokenDto;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.util.exception.TokenNotFound;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.NOT_FOUND_TOKEN;

@Service
@Transactional
public class LogoutService implements LogoutHandler {

    private final TokenService tokenService;

    public LogoutService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<TokenDto> tokenDtoOptional = tokenService.mapToTokenDto(request);
        TokenDto tokenDto = tokenDtoOptional.orElseThrow(() -> new TokenNotFound(NOT_FOUND_TOKEN));

        Optional<Token> tokenOptional = tokenService.findToken(tokenDto.getRefreshToken());
        Token token = tokenOptional.orElseThrow(() -> new TokenNotFound(NOT_FOUND_TOKEN));

        Account account = token.getAccount();
        tokenService.revokeAllTokens(account.toUserDetails());

        Cookie accessToken = new Cookie(TokenService.ACCESS_TOKEN_NAME, null);
        Cookie refreshToken = new Cookie(TokenService.REFRESH_TOKEN_NAME, null);

        accessToken.setMaxAge(0);
        refreshToken.setMaxAge(0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        try {
            response.getWriter().write("Completed signout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
