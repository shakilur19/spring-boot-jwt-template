package com.jwttemplate.api.security_config;

import com.jwttemplate.api.auth.entity.User;
import com.jwttemplate.api.security_config.model.AuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-validity-minutes:1440}")
    private long accessTokenValidityMinutes;

    @Value("${jwt.refresh-token-validity-minutes:10080}")
    private long refreshTokenValidityMinutes;

    public AuthToken generateToken(User user) {
        Map<String, Object> claims = buildClaims(user);
        String accessToken = createAccessToken(claims);
        String refreshToken = createRefreshToken(claims);
        return new AuthToken(accessToken, refreshToken);
    }

    public AuthToken generateTokenFromClaims(Claims claims) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", claims.get("id"));
        claimsMap.put("email", claims.get("email"));
        claimsMap.put("firstName", claims.get("firstName"));
        claimsMap.put("lastName", claims.get("lastName"));

        return new AuthToken(
                createAccessToken(claimsMap),
                createRefreshToken(claimsMap)
        );
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String createAccessToken(Map<String, Object> claims) {
        return createToken(claims, accessTokenValidityMinutes);
    }

    private String createRefreshToken(Map<String, Object> claims) {
        return createToken(claims, refreshTokenValidityMinutes);
    }

    private String createToken(Map<String, Object> claimsMap, long validityInMinutes) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + TimeUnit.MINUTES.toMillis(validityInMinutes));

        Claims claims = Jwts.claims()
                .subject(claimsMap.get("email").toString())
                .id(claimsMap.get("id").toString())
                .add(claimsMap)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSignKey())
                .compact();
    }

    private Map<String, Object> buildClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        return claims;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}