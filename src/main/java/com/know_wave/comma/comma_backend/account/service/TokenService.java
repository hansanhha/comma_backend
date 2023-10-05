package com.know_wave.comma.comma_backend.account.service;

import com.know_wave.comma.comma_backend.account.dto.TokenDto;
import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.entity.token.Token;
import com.know_wave.comma.comma_backend.account.repository.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.security.KeyPair;
import java.util.*;

@Service
@Transactional
public class TokenService {

    @Value("${SECRET_KEY}")
    private String key;
    private final KeyPair keyPair;
    @Value("${ACCESS_TOKEN_EXPIRATION}")
    private int accessTokenExpiration;
    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private int refreshTokenExpiration;

    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NOT_FOUND_MESSAGE = "NotFound refresh token";
    public static final String ACCESS_TOKEN_NOT_FOUND_MESSAGE = "NotFound access token";
    public static final String REFRESH_TOKEN_EXPIRED_MESSAGE = "Expired refresh token. login is required";
    public static final String REFRESH_TOKEN_INVALID_MESSAGE = "Invalid refresh token";
    public static final String ACCESS_TOKEN_EXPIRED_MESSAGE = "Expired access token. re-issuance is required";

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    public UserDetails toUserDetails(String token) {
        Token findToken = tokenRepository.findByToken(token);
        return findToken.getAccount().toUserDetails();
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

    public void revokeAllTokens(UserDetails user) {
        List<Token> tokenList = tokenRepository.findAllValidByAccount(user.getUsername());
        tokenList.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
    }

    public Optional<Token> findToken(String token) {
        return Optional.ofNullable(tokenRepository.findByToken(token));
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

    public boolean isValidToken(Token token) {

        if (Objects.nonNull(token)) {
            Date tokenExpiration = token.getExpiration();
            return new Date().before(tokenExpiration) && !token.isExpired() && !token.isRevoked();
        }
        return false;
    }

    public boolean isSameUser(String accessToken, Token refreshToken) {
        Jws<Claims> accessClaimsJws = parseClaims(accessToken);
        Claims accessClaims = accessClaimsJws.getBody();

        String accessUserId = accessClaims.getSubject();
        String refreshUserId = refreshToken.getAccount().getId();

        return accessUserId.equals(refreshUserId);
    }

    public Optional<TokenDto> mapToTokenDto(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        HashMap<String, String> tokenMap = new HashMap<>();

        Arrays.stream(cookies)
                .forEach(cookie -> {
                    if (cookie.getName().equals(ACCESS_TOKEN_NAME) && Objects.nonNull(cookie.getValue())) {
                        tokenMap.put(ACCESS_TOKEN_NAME, cookie.getValue());
                    }

                    if (cookie.getName().equals(REFRESH_TOKEN_NAME) && Objects.nonNull(cookie.getValue())) {
                        tokenMap.put(REFRESH_TOKEN_NAME, cookie.getValue());
                    }
                });

        if (tokenMap.size() != 2) {
            return Optional.empty();
        }

        return Optional.of(new TokenDto(tokenMap.get(ACCESS_TOKEN_NAME), tokenMap.get(REFRESH_TOKEN_NAME)));
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

    public boolean isExpired(String token) {
        try {
            Jws<Claims> claimsJws = parseClaims(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    public boolean isEmptyToken(String token) {
        Jws<Claims> claimsJws = parseClaims(token);
        return claimsJws.getBody().isEmpty();
    }

    private Key getSignKey() {
        byte[] decoded = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(decoded);
    }
}
