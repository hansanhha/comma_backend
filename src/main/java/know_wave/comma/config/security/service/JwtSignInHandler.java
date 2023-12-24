package know_wave.comma.config.security.service;

import io.jsonwebtoken.Claims;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.dto.SignInResponse;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.config.security.exception.InvalidTokenException;
import know_wave.comma.config.security.exception.NotFoundTokenException;
import know_wave.comma.config.security.exception.SignInFailureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtSignInHandler implements TokenSignInHandler<SignInResponse> {

    private final JwtTokenHandler tokenService;
    private final AccountUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignInResponse signIn(String accountId, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new SignInFailureException(ExceptionMessageSource.FAIL_SIGN_IN);
        }

        tokenService.revokeRefreshToken(accountId);

        String accessToken = tokenService.issueAccessToken(accountId);
        String refreshToken = tokenService.issueRefreshToken(accountId);

        return SignInResponse.create(accessToken, refreshToken);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Claims refreshTokenPayload = tokenService.getPayload(refreshToken);

        Optional<Token> findToken = tokenService.getRefreshToken(refreshToken);

        if(findToken.isEmpty()) {
            throw new NotFoundTokenException(ExceptionMessageSource.NOT_FOUND_TOKEN);
        }

        if (!tokenService.isValidRefreshToken(refreshToken)) {
            throw new InvalidTokenException(ExceptionMessageSource.INVALID_TOKEN);
        }

        String accountId = refreshTokenPayload.getSubject();

        return tokenService.issueAccessToken(accountId);
    }
}
