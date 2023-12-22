package know_wave.comma.config.security.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.config.security.exception.TokenExpiredException;
import know_wave.comma.config.security.exception.TokenTemperedException;
import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService<T> {

    String issueAccessToken(String username);

    String issueRefreshToken(String username);

    void revokeRefreshToken(String username);

    T getPayload(String token) throws TokenExpiredException, TokenTemperedException;

    boolean isValidRefreshToken(String token);

}
