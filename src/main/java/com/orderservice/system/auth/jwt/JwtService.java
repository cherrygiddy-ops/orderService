package com.orderservice.system.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtConfig config;

    public Jwt generateAccessToken(String subject, String role) {
        return generateToken(subject, role, config.getAccessExpiration());
    }

    public Jwt generateRefreshToken(String subject, String role) {
        return generateToken(subject, role, config.getRefreshExpiration());
    }

    private Jwt generateToken(String subject, String role, int expirationSeconds) {
        String token = Jwts.builder()
                .subject(subject)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * expirationSeconds))
                .signWith(config.getSecretKey())
                .compact();

        Claims claims = Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new Jwt(token, claims);
    }

    public Jwt parseToken(String token) {
        try {
            Claims claims = getClaims(token);
            return new Jwt(token, claims);
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid token: " + ex.getMessage(), ex);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(config.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
