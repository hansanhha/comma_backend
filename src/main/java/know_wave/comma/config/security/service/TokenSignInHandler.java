package know_wave.comma.config.security.service;

public interface TokenSignInHandler<T> {

    T signIn(String accountId, String password);

    String reissueAccessToken(String refreshToken);
}
