package know_wave.comma.config.security.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.config.security.entity.Token;
import know_wave.comma.account.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.security.KeyPair;
import java.util.*;

@Service
@Transactional
@Slf4j
public class TokenService {

    @Value("${SECRET_KEY}")
    private String key;
    private final KeyPair keyPair;
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private long accessTokenExpiration;
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpiration;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(Map<String, Object> claims , UserDetails userDetails) {
        return buildToken(claims, userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    public void revokeAllRefreshTokens(Account account) {
        List<Token> tokenList = tokenRepository.findAllValidByAccount(account);
        tokenList.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
    }

    public boolean existValidRefreshToken(Account account) {
        return !tokenRepository.findAllValidByAccount(account).isEmpty();
    }

    public void saveToken(String refreshToken) {

        Jws<Claims> claimsJws = parseClaims(refreshToken);
        Claims claims = claimsJws.getBody();

        Token token = new Token(refreshToken,
                false,
                false,
                claims.getExpiration(),
                Account.builder().id(claims.getSubject()).build());

        tokenRepository.save(token);
    }

    public Optional<Claims> toAccessTokenClaims(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        log.info("authorizationHeader: {}", authorizationHeader);

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String accessTokenString = authorizationHeader.substring(7);
            return Optional.of(parseClaims(accessTokenString).getBody());
        }

        return Optional.empty();
    }

    private String buildToken(Map<String, Object> claims,
                              UserDetails userDetails,
                              long expiration) {

        long now = System.currentTimeMillis();

        return Jwts.
                builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValidClaims(Claims claims) {

        if (!claims.isEmpty() && !claims.getSubject().isEmpty()) {
            Date expiration = claims.getExpiration();
            return new Date().before(expiration);
        }

        return false;
    }

    public void checkRefreshToken(Account account) {
        List<Token> tokenList = tokenRepository.findAllValidByAccount(account);

        Date now = new Date();

        tokenList.forEach(token -> {
            if (now.after(token.getExpiration())) {
                token.setExpired(true);
            }
        });
    }

    private Key getSignKey() {
        byte[] decoded = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(decoded);
    }
}
