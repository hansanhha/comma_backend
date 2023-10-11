package com.know_wave.comma.comma_backend.security.filter;

import com.know_wave.comma.comma_backend.account.dto.TokenDto;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.service.auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final HandlerMappingIntrospector introspector;
    private List<RequestMatcher> matchers;

    public JwtAuthenticationFilter(TokenService tokenService, HandlerMappingIntrospector introspector) {
        this.tokenService = tokenService;
        this.introspector = introspector;
    }

    public void requestMatchers(List<RequestMatcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<TokenDto> tokenDtoOptional = tokenService.mapToTokenDto(request);
        if (tokenDtoOptional.isEmpty()) {
            sendErrorResponse(response, INVALID_TOKEN);
            return;
        }

        final TokenDto tokenDto = tokenDtoOptional.get();
        final String accessToken = tokenDto.getAccessToken();
        final String refreshToken = tokenDto.getRefreshToken();

        if (tokenService.isExpired(accessToken) ||
                tokenService.isEmpty(accessToken) ||
                tokenService.isEmpty(refreshToken)) {
            sendErrorResponse(response, INVALID_TOKEN);
            return;
        }

        Optional<Token> findTokenOptional = tokenService.findToken(refreshToken);
        if (findTokenOptional.isEmpty()) {
            sendErrorResponse(response, INVALID_TOKEN);
            return;
        }

        final Token findToken = findTokenOptional.get();
        if (tokenService.isTempered(accessToken, findToken)) {
            sendErrorResponse(response, TEMPERED_TOKEN);
            return;
        }

        UserDetails accountDetails = tokenService.toUserDetails(findToken.getToken());

        var authentication = new UsernamePasswordAuthenticationToken(accountDetails, null, accountDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private static void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}
