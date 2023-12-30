package know_wave.comma.config.security.service;

import know_wave.comma.config.security.exception.TokenExpiredException;
import know_wave.comma.config.security.exception.TokenTemperedException;

public interface TokenHandler<T> {

    String issueAccessToken(String username);

    String issueRefreshToken(String username);

    void revokeRefreshToken(String username);

    T getPayload(String token) throws TokenExpiredException, TokenTemperedException;

    boolean isValidRefreshToken(String token);

}
