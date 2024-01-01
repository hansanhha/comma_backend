package know_wave.comma.config.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.service.JwtTokenHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static know_wave.comma.config.security.service.JwtTokenHandler.ROLE_KEY_NAME;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenHandler jwtTokenService;

    public static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> optionalAccessToken = getAccessToken(request);

        if (optionalAccessToken.isEmpty()) {
            sendErrorResponse(response, ExceptionMessageSource.INVALID_TOKEN);
            return;
        }

        Claims claims = null;

        try {
            claims = jwtTokenService.getPayload(optionalAccessToken.get());
        } catch (Exception e) {
            sendErrorResponse(response, e.getMessage());
        }

        String username = claims.getSubject();
        String role = (String) claims.get(ROLE_KEY_NAME);

        var authentication = UsernamePasswordAuthenticationToken
                .authenticated(username, null, List.of(() -> role));

        // HTTP request 정보 포함
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

    private Optional<String> getAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String accessToken = authorizationHeader.substring(TOKEN_PREFIX.length());
            return Optional.of(accessToken);
        }

        return Optional.empty();
    }

    private static void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        return requestURI.equals("/account/signin") ||
                requestURI.equals("/account/signup") ||
                requestURI.equals("/account/email/verify/request") ||
                requestURI.equals("/account/email/verify") ||
                requestURI.equals("/account/refresh-token") ||
                requestURI.startsWith("/arduinos") &&
                                request.getMethod().equals(HttpMethod.GET.name()) ||
                requestURI.startsWith("/payment-cb") ||
                request.getMethod().equals(HttpMethod.OPTIONS.name());
    }
}
