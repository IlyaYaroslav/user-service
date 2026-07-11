package com.taskspace.userservice.service;

import com.taskspace.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms}")
    private Long expirationMs;

    public String generateToken(User user) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);
        return Jwts.builder().setSubject(user.getId().toString())
                .setHeaderParam("typ", "JWT")
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("first_name", user.getFirstName())
                .claim("last_name", user.getLastName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(getAllClaims(token).getSubject());
    }

    public String extractRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
