package know_wave.comma.config.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.repository.TokenRepository;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.config.security.entity.SecurityAccount;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.config.security.exception.NotFoundTokenException;
import know_wave.comma.config.security.exception.TokenExpiredException;
import know_wave.comma.config.security.exception.TokenTemperedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtTokenService implements TokenService<Claims> {

    private final TokenRepository tokenRepository;
    private final AccountQueryService accountQueryService;

    public static final String ROLE_KEY_NAME = "ROLE";

    @Value("${TOKEN_SECRET_KEY}")
    private String key;
    private Key secrectKey;

    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long accessTokenExpiration;

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpiration;

    @PostConstruct
    public void init() {
        byte[] decoded = Decoders.BASE64.decode(key);
        secrectKey = Keys.hmacShaKeyFor(decoded);
    }

    @Override
    public String issueAccessToken(String username) {
        Account account = accountQueryService.findAccount(username);
        SecurityAccount securityAccount = SecurityAccount.to(account);

        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + accessTokenExpiration);

        return generateToken(
                username,
                Map.of(ROLE_KEY_NAME, getRole(securityAccount)),
                issuedAt,
                expiredAt);
    }

    @Override
    public String issueRefreshToken(String username) {
        Account account = accountQueryService.findAccount(username);
        SecurityAccount securityAccount = SecurityAccount.to(account);

        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + refreshTokenExpiration);

        String refreshToken = generateToken(
                username,
                Map.of(ROLE_KEY_NAME, getRole(securityAccount)),
                issuedAt,
                expiredAt);

        Token token = Token.create(account, refreshToken, expiredAt);
        tokenRepository.save(token);

        return refreshToken;
    }

    @Override
    public Claims getPayload(String token) throws TokenExpiredException, TokenTemperedException {
        token = token.replaceAll("\\s+", "").trim();
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secrectKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(ExceptionMessageSource.EXPIRED_TOKEN);
        } catch (SignatureException e) {
            throw new TokenTemperedException(ExceptionMessageSource.TEMPERED_TOKEN);
        }
    }

    @Override
    public void revokeRefreshToken(String username) {
        Account account = accountQueryService.findAccount(username);
        List<Token> tokenList= tokenRepository.findAllByAccount(account);

        tokenList.forEach(token -> token.setRevoked(true));
    }

    @Override
    public boolean isValidRefreshToken(String token) {
        Token findToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundTokenException(ExceptionMessageSource.NOT_FOUND_TOKEN));

        return findToken.getToken().equals(token) || !findToken.isRevoked();
    }

    public Optional<Token> getRefreshToken(String refreshToken) {
        return tokenRepository.findByToken(refreshToken);
    }

    private String getRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_ROLE));
    }

    private String generateToken(String username, Map<String, String> role, Date issuedAt,Date expiredAt) {
        return Jwts.
                builder()
                .setClaims(role)
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(secrectKey)
                .compact();
    }
}
