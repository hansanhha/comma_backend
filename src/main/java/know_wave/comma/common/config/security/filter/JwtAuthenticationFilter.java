package know_wave.comma.common.config.security.filter;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.common.config.security.service.TokenService;
import know_wave.comma.common.config.security.config.PermitRequestMatcherConfig;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import know_wave.comma.message.util.ExceptionMessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final TokenService tokenService;
    private final AccountQueryService accountQueryService;
    private final PermitRequestMatcherConfig permitRequestMatcherConfig;

    public JwtAuthenticationFilter(TokenService tokenService, AccountQueryService accountQueryService, PermitRequestMatcherConfig permitRequestMatcherConfig) {
        this.tokenService = tokenService;
        this.accountQueryService = accountQueryService;
        this.permitRequestMatcherConfig = permitRequestMatcherConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<Claims> optionalAccessClaims = tokenService.toAccessTokenClaims(request);

        if (optionalAccessClaims.isPresent()) {
            Claims accessClaims = optionalAccessClaims.get();

            boolean valid = tokenService.isValidClaims(accessClaims);

            if (valid) {
                Account account = accountQueryService.findAccount(accessClaims.getSubject());
                UserDetails userDetails = account.toUserDetails();

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
                return;
            }

        }

        sendErrorResponse(response, ExceptionMessageSource.INVALID_TOKEN);
    }

    private static void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return permitRequestMatcherConfig.isPermitRequest(request);
    }
}
