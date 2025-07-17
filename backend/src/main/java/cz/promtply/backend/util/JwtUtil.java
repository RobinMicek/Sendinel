package cz.promtply.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key key;
    private final long expiration;

    public JwtUtil(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration}") long expiration) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters for HS256.");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String generateToken(String email, String role, boolean totpVerified) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("totp", totpVerified);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTotpVerified(String token) {
        return parseToken(token).get("totp", Boolean.class);
    }

    public String getEmail(String token) {
        return parseToken(token).getSubject();
    }
}