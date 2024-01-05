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

    private final JwtTokenHandler tokenHandler;
    private final AccountUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignInResponse signIn(String accountId, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new SignInFailureException(ExceptionMessageSource.FAIL_SIGN_IN);
        }

        tokenHandler.revokeRefreshToken(accountId);

        String accessToken = tokenHandler.issueAccessToken(accountId);
        String refreshToken = tokenHandler.issueRefreshToken(accountId);

        return SignInResponse.create(accessToken, refreshToken);
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        Claims refreshTokenPayload = tokenHandler.getPayload(refreshToken);

        Optional<Token> findToken = tokenHandler.getToken(refreshToken);

        if(findToken.isEmpty()) {
            throw new NotFoundTokenException(ExceptionMessageSource.NOT_FOUND_TOKEN);
        }

        if (!tokenHandler.isValidRefreshToken(refreshToken)) {
            throw new InvalidTokenException(ExceptionMessageSource.INVALID_TOKEN);
        }

        String accountId = refreshTokenPayload.getSubject();

        return tokenHandler.issueAccessToken(accountId);
    }
}
