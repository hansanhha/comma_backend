package know_wave.comma.config.security.service;

public interface TokenSignInService<T> {

    T signIn(String accountId, String password);

    String refreshToken(String refreshToken);
}
