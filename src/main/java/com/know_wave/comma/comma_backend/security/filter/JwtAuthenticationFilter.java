package com.know_wave.comma.comma_backend.security.filter;

import com.know_wave.comma.comma_backend.account.dto.TokenDto;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final HandlerMappingIntrospector introspector;
    private List<RequestMatcher> matchers;

    public JwtAuthenticationFilter(TokenService tokenService, HandlerMappingIntrospector introspector) {
        this.tokenService = tokenService;
        this.introspector = introspector;
    }

    public void requestMatchers(String... pattern) {
        createMvcMatchers(pattern);
    }

    // if문, try-catch문 고민

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<TokenDto> tokenDtoOptional = tokenService.mapToTokenDto(request);
        if (tokenDtoOptional.isEmpty()) {
            sendErrorResponse(response, "NotFound token");
            return;
        }

        final TokenDto tokenDto = tokenDtoOptional.get();
        final String accessToken = tokenDto.getAccessToken();
        final String refreshToken = tokenDto.getRefreshToken();

        if (tokenService.isExpired(accessToken)) {
            sendErrorResponse(response, "Expired access token");
            return;
        }

        Optional<Token> findTokenOptional = tokenService.findToken(refreshToken);
        if (findTokenOptional.isEmpty()) {
            sendErrorResponse(response, "NotExist token");
            return;
        }

        final Token findToken = findTokenOptional.get();
        if (!tokenService.isSameUser(accessToken, findToken)) {
            sendErrorResponse(response, "Tempered Token");
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

    private void createMvcMatchers(String... patterns) {
        matchers = new ArrayList<>(patterns.length);

        for (String pattern : patterns) {
            MvcRequestMatcher matcher = new MvcRequestMatcher(introspector, pattern);
            matchers.add(matcher);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}
